package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.model.answer.JuridicalAnswer;
import by.x1ss.adapterservice.model.answer.PhysicalAnswer;
import by.x1ss.adapterservice.model.request.Request;
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


    public JuridicalAnswer getJuridicalAnswer(String inn) {
        Request request = new Request(inn, true);
        log.info("AdapterService got juridical request {}", request);
        HttpStatus httpStatus = restTemplate.postForObject(links.getRequest(), request, HttpStatus.class);
        log.info("AdapterService got juridical post {}", httpStatus);
        if (httpStatus == HttpStatus.PROCESSING) {
            ResponseEntity<JuridicalAnswer> responseEntity = restTemplate.getForEntity(links.getJuridicalGetAnswer() + request.getUuid(), JuridicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity(links.getJuridicalGetAnswer() + request.getUuid(), JuridicalAnswer.class);
            }
            log.info("AdapterService got juridical answer {}", responseEntity.getBody());
            restTemplate.delete(links.getJuridicalConfirm() + request.getUuid());
            return responseEntity.getBody();
        }
        return null;
    }

    public PhysicalAnswer getPhysicalAnswer(String sts) {
        Request request = new Request(sts, false);
        log.info("AdapterService got physical request {}", request.getStr());
        HttpStatus httpStatus = restTemplate.postForObject(links.getRequest(), request, HttpStatus.class);
        log.info("AdapterService got physical post {}", httpStatus);
        if (httpStatus == HttpStatus.PROCESSING) {
            ResponseEntity<PhysicalAnswer> responseEntity = restTemplate.getForEntity(links.getPhysicalGetAnswer() + request.getUuid(), PhysicalAnswer.class);
            while (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseEntity = restTemplate.getForEntity(links.getPhysicalGetAnswer() + request.getUuid(), PhysicalAnswer.class);
            }
            log.info("AdapterService got physical answer {}", responseEntity.getBody());
            restTemplate.delete(links.getPhysicalConfirm() + request.getUuid());
            return responseEntity.getBody();
        }
        return null;
    }
}
