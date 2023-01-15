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
            JuridicalAnswer answer = responseEntity.getBody();
            restTemplate.delete("http://localhost:8081/smev/response/juridical/" + request.getValue() + "/confirm");
            return answer;

        }
        return null;
    }

    public PhysicalAnswer getPhysicalAnswer(PersonRequest request) {
        restTemplate.postForLocation("http://localhost:8081/smev/request/physical/", request.getValue());
        ResponseEntity<?> entity = restTemplate.getForEntity("http://localhost:8081/smev/response/physical", ResponseEntity.class);
        while (entity.getStatusCodeValue() != 404) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            entity = restTemplate.getForEntity("http://localhost:8081/smev/response/physical", ResponseEntity.class);
        }
        return (PhysicalAnswer) entity.getBody();
    }
}
