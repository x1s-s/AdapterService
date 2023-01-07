package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.model.Answer;
import by.x1ss.adapterservice.model.request.requestImpl.RequestFromJuridical;
import by.x1ss.adapterservice.service.AdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/adapter")
public class AdapterController {
    @Autowired
    private AdapterService adapterService;

    @GetMapping("/answer/juridical/")
    public ResponseEntity<?> getJuridicalAnswer(@RequestBody RequestFromJuridical request) {
        Answer answer;
        try {
            answer = adapterService.getAnswer(request).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return ResponseEntity.status(504).body("Timeout error");
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping("/answer/physical/")
    public ResponseEntity<?> getPhysicalAnswer(@RequestBody RequestFromJuridical request) {
        Answer answer;
        try {
            answer = adapterService.getAnswer(request).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return ResponseEntity.status(504).body("Timeout error");
        }
        return ResponseEntity.ok().body(answer);
    }
}
