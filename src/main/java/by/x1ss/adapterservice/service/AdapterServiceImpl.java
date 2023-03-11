package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.DTO.ResponseList;
import by.x1ss.adapterservice.aop.ConnectionCheckAnnotation;
import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.exception.NotFoundInSmevException;
import by.x1ss.adapterservice.exception.SmevEcxeption;
import by.x1ss.adapterservice.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
public class AdapterServiceImpl implements AdapterService {
    private final LinksToOtherService links;
    private final RestTemplate restTemplate;

    @Autowired
    public AdapterServiceImpl(LinksToOtherService links, RestTemplate restTemplate) {
        this.links = links;
        this.restTemplate = restTemplate;
    }


    @ConnectionCheckAnnotation
    public ResponseList getAnswer(String clientIdentifier, Boolean isJuridical){
        Request request = new Request(clientIdentifier, isJuridical);
        log.info("AdapterService got request {}", request);
        ResponseEntity<?> responseStatus = restTemplate.postForEntity(links.getRequest(), request, ResponseEntity.class);
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
