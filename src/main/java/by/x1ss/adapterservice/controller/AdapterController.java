package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.model.answer.JuridicalAnswer;
import by.x1ss.adapterservice.model.answer.PhysicalAnswer;
import by.x1ss.adapterservice.model.request.requestImpl.RequestFromJuridical;
import by.x1ss.adapterservice.model.request.requestImpl.RequestFromPhysical;
import by.x1ss.adapterservice.service.AdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adapter")
public class AdapterController {
    @Autowired
    private AdapterService adapterService;

    @GetMapping("/answer/juridical/")
    public ResponseEntity<?> getJuridicalAnswer(@RequestBody RequestFromJuridical request) {
        JuridicalAnswer answer = adapterService.getJuridicalAnswer(request);
        if(answer != null) {
            return ResponseEntity.ok(answer);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/answer/physical/")
    public ResponseEntity<?> getPhysicalAnswer(@RequestBody RequestFromPhysical request) {
        PhysicalAnswer answer = adapterService.getPhysicalAnswer(request);
        if(answer != null) {
            return ResponseEntity.ok(answer);
        }
        return ResponseEntity.badRequest().build();
    }
}
