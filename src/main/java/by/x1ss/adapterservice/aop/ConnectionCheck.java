package by.x1ss.adapterservice.aop;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.exception.SmevEcxeption;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

    @Pointcut("@annotation(ConnectionCheckAnnotation)")
    public void callCheckConnectionAnnotation() {
    }

    @Before("callCheckConnectionAnnotation()")
    public void beforeAnnotation() {
        ResponseEntity<String> status = restTemplate.getForEntity(links.getStatus(), String.class);
        log.info("AdapterService got status from SMEV service {}", status.getBody());
        if (status.getStatusCode() != HttpStatus.OK && status.getBody() != null && !status.getBody().contains("UP")) {
            throw new SmevEcxeption("SMEV service is down", status.getStatusCode());
        }

    }
}
