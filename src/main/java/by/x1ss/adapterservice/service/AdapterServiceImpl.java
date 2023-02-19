package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.exception.SmevEcxeption;
import by.x1ss.adapterservice.model.Request;
import by.x1ss.adapterservice.model.Response;
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


    public Response getAnswer(String clientIdentifier, Boolean isJuridical){
        Request request = new Request(clientIdentifier, isJuridical);
        log.info("AdapterService got request {}", request);
        checkServerStatus();
        ResponseEntity<?> responseStatus = restTemplate.postForEntity(links.getRequest(), request, ResponseEntity.class);
        log.info("AdapterService got post {}", responseStatus);
        if (responseStatus.getStatusCode() == HttpStatus.ACCEPTED) {
            ResponseEntity<Response> responseEntity = getResponse(request.getUuid());
            log.info("AdapterService got response {}", responseEntity.getBody());
            restTemplate.delete(links.getConfirm() + request.getUuid());
            log.info("AdapterService delete response with {}", request.getUuid());
            return responseEntity.getBody();
        } else {
            if(request.getIsJuridical()){
                throw new SmevEcxeption("SMEV service cant find company with this inn", HttpStatus.BAD_REQUEST);
            } else {
                throw new SmevEcxeption("SMEV service cant find person with this sts", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void checkServerStatus(){
        ResponseEntity<String> status = restTemplate.getForEntity(links.getStatus(), String.class);
        log.info("AdapterService got status {}", status.getBody());
        if(status.getStatusCode() != HttpStatus.OK && status.getBody() != null  && !status.getBody().contains("UP")){
            throw new SmevEcxeption("SMEV service is down", status.getStatusCode());
        }
    }

    private ResponseEntity<Response> getResponse(UUID uuid){
        ResponseEntity<Response> responseEntity = restTemplate.getForEntity(links.getGetAnswer() + uuid, Response.class);
        while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("AdapterService sleep error (interrupted)", e);
            }
            responseEntity = restTemplate.getForEntity(links.getGetAnswer() + uuid, Response.class);
        }
        return responseEntity;
    }

}