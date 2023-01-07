package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.model.Answer;
import by.x1ss.adapterservice.model.request.PersonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class AdapterService {
    private final RestTemplate restTemplate;

    public AdapterService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Answer> getAnswer(PersonRequest request) {
        Answer answer = restTemplate.getForObject(
                "http://localhost:8081/answer"+ request.getType()
                        + "/?value=" + request.getValue(), Answer.class);
        return CompletableFuture.completedFuture(answer);
    }
}
