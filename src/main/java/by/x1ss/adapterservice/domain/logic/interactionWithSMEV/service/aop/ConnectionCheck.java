package by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.aop;

import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.configuration.propertiesConfig.LinksToOtherService;
import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.exception.SmevEcxeption;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Aspect
@Component
@Slf4j
public class ConnectionCheck {
    final RestTemplate restTemplate;
    final LinksToOtherService links;

    public ConnectionCheck(RestTemplate restTemplate, LinksToOtherService links) {
        this.restTemplate = restTemplate;
        this.links = links;
    }

    @Pointcut("@annotation(by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.aop.ConnectionCheckAnnotation)")
    public void callCheckConnectionAnnotation() {
    }

    @Before("callCheckConnectionAnnotation()")
    public void beforeAnnotation() {
        try {
            log.info("AdapterService try to get response from SEMV service");
            ResponseEntity<String> status = restTemplate.getForEntity(links.getStatus(), String.class);
            log.info("AdapterService got status from SMEV service {}", status.getBody());
            if (status.getStatusCode() != HttpStatus.OK && status.getBody() != null && !status.getBody().contains("UP")) {
                log.warn("SMEW service isn't up {}", status);
                throw new SmevEcxeption("SMEV service isn't up", status.getStatusCode());
            }
        } catch (RestClientException e){
            log.warn("SMEV service isn't response");
            throw new SmevEcxeption("SMEV service isn't response", HttpStatus.SERVICE_UNAVAILABLE);
        }

    }
}
