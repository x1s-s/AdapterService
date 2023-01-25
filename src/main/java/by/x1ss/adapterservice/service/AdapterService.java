package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.model.answer.JuridicalAnswer;
import by.x1ss.adapterservice.model.answer.PhysicalAnswer;
import by.x1ss.adapterservice.model.request.PersonRequest;
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


    public JuridicalAnswer getJuridicalAnswer(PersonRequest request) {
        log.info("AdapterService got juridical request {}", request.getValue());
        HttpStatus httpStatus = restTemplate.postForObject(links.getJuridicalRequest(), request.getValue(), HttpStatus.class);
        log.info("AdapterService got juridical post {}", httpStatus);
        if (httpStatus == HttpStatus.PROCESSING) {
            ResponseEntity<JuridicalAnswer> responseEntity = restTemplate.getForEntity(links.getJuridicalGetAnswer() + request.getValue(), JuridicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity(links.getJuridicalGetAnswer() + request.getValue(), JuridicalAnswer.class);
            }
            log.info("AdapterService got juridical answer {}", responseEntity.getBody());
            restTemplate.delete(links.getJuridicalGetAnswer() + request.getValue() + "/confirm");
            return responseEntity.getBody();
        }
        return null;
    }

    public PhysicalAnswer getPhysicalAnswer(PersonRequest request) {
        log.info("AdapterService got physical request {}", request.getValue());
        HttpStatus httpStatus = restTemplate.postForObject(links.getPhysicalRequest(), request.getValue(), HttpStatus.class);
        log.info("AdapterService got physical post {}", httpStatus);
        if (httpStatus == HttpStatus.PROCESSING) {
            ResponseEntity<PhysicalAnswer> responseEntity = restTemplate.getForEntity(links.getPhysicalGetAnswer() + request.getValue(), PhysicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity(links.getPhysicalGetAnswer() + request.getValue(), PhysicalAnswer.class);
            }
            log.info("AdapterService got physical answer {}", responseEntity.getBody());
            restTemplate.delete(links.getPhysicalGetAnswer() + request.getValue() + "/confirm");
            return responseEntity.getBody();
        }
        return null;
    }
}
