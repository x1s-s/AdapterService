package by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service;

import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.api.ServiceControllerAPI;
import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.aop.ConnectionCheckAnnotation;
import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.configuration.propertiesConfig.LinksToOtherService;
import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.exception.NotFoundInSmevException;
import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.exception.SmevEcxeption;
import by.x1ss.adapterservice.domain.object.Request;
import by.x1ss.adapterservice.domain.object.ResponseList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
public class ServiceControllerAPIImpl implements ServiceControllerAPI {
    private final LinksToOtherService links;
    private final RestTemplate restTemplate;

    @Autowired
    public ServiceControllerAPIImpl(LinksToOtherService links, RestTemplate restTemplate) {
        this.links = links;
        this.restTemplate = restTemplate;
    }


    //disclaimer! annotation only for learn how to use AOP
    @ConnectionCheckAnnotation
    public ResponseList getAnswer( String clientIdentifier, Boolean isJuridical){
        Request request = new Request(clientIdentifier, isJuridical);
        log.info("AdapterService got request {}", request);
        ResponseEntity<Object> responseStatus = restTemplate.postForEntity(links.getRequest(), request, Object.class);
        log.info("AdapterService got post {}", responseStatus);
        if (responseStatus.getStatusCode() == HttpStatus.ACCEPTED) {
            ResponseEntity<ResponseList> responseEntity = getResponse(request.getUuid());
            log.info("AdapterService got response {}", responseEntity.getBody());
            restTemplate.delete(links.getConfirm() + request.getUuid());
            log.info("AdapterService delete response with {}", request.getUuid());
            return responseEntity.getBody();
        } else {
            throw new SmevEcxeption("SMEV service return error", responseStatus.getStatusCode());
        }
    }

    private ResponseEntity<ResponseList> getResponse(UUID uuid){
        ResponseEntity<ResponseList> response = restTemplate.getForEntity(links.getGetAnswer() + uuid, ResponseList.class);
        while (true) {
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                return response;
            }
            if(response.getStatusCode() == HttpStatus.NO_CONTENT){
                throw new NotFoundInSmevException();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("AdapterService sleep error (interrupted)", e);
            }
            response = restTemplate.getForEntity(links.getGetAnswer() + uuid, ResponseList.class);
        }
    }

}
