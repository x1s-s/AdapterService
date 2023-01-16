package by.x1ss.adapterservice.service;

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
    private final RestTemplate restTemplate;

    public AdapterService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JuridicalAnswer getJuridicalAnswer(PersonRequest request) {
        log.info("AdapterService got juridical request {}", request.getValue());
        HttpStatus httpStatus = restTemplate.postForObject("http://localhost:8081/smev/request/juridical/", request.getValue(), HttpStatus.class);
        log.info("AdapterService got juridical post {}", httpStatus);
        if(httpStatus == HttpStatus.OK) {
            ResponseEntity<JuridicalAnswer> responseEntity = restTemplate.getForEntity("http://localhost:8081/smev/response/juridical/" + request.getValue(), JuridicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity("http://localhost:8081/smev/response/juridical/" + request.getValue(), JuridicalAnswer.class);
            }
            log.info("AdapterService got juridical answer {}", responseEntity.getBody());
            restTemplate.delete("http://localhost:8081/smev/response/juridical/" + request.getValue() + "/confirm");
            return responseEntity.getBody();
        }
        return null;
    }

    public PhysicalAnswer getPhysicalAnswer(PersonRequest request) {
        log.info("AdapterService got physical request {}", request.getValue());
        HttpStatus httpStatus = restTemplate.postForObject("http://localhost:8081/smev/request/physical/", request.getValue(), HttpStatus.class);
        log.info("AdapterService got physical post {}", httpStatus);
        if(httpStatus == HttpStatus.OK) {
            ResponseEntity<PhysicalAnswer> responseEntity = restTemplate.getForEntity("http://localhost:8081/smev/response/physical/" + request.getValue(), PhysicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK) {
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity("http://localhost:8081/smev/response/physical/" + request.getValue(), PhysicalAnswer.class);
            }
            log.info("AdapterService got juridical answer {}", responseEntity.getBody());
            restTemplate.delete("http://localhost:8081/smev/response/physical/" + request.getValue() + "/confirm");
            return responseEntity.getBody();
        }
        return null;
    }
}
