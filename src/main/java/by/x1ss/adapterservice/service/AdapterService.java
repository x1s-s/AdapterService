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

@Service
@Slf4j
public class AdapterService {
    @Autowired
    private LinksToOtherService links;
    @Autowired
    private RestTemplate restTemplate;

    public Response getResponse(String clientIdentifier, Boolean isJuridical){
        Request request = new Request(clientIdentifier, isJuridical);
        log.info("AdapterService got request {}", request);
        HttpStatus httpStatus = restTemplate.postForObject(links.getRequest(), request, HttpStatus.class);
        log.info("AdapterService got post {}", httpStatus);
        if (httpStatus == HttpStatus.PROCESSING) {
            ResponseEntity<Response> responseEntity = restTemplate.getForEntity(links.getGetAnswer() + request.getUuid(), Response.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity(links.getGetAnswer() + request.getUuid(), Response.class);
            }
            log.info("AdapterService got response {}", responseEntity.getBody());
            return responseEntity.getBody();
        } else {
            throw new SmevEcxeption("Smev service exception", httpStatus );
        }
    }

}
