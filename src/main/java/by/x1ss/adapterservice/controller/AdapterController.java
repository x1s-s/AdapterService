package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.model.answer.JuridicalAnswer;
import by.x1ss.adapterservice.model.answer.PhysicalAnswer;
import by.x1ss.adapterservice.service.AdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adapter")
public class AdapterController {
    @Autowired
    private AdapterService adapterService;

    @GetMapping("/answer/juridical/{inn}")
    public ResponseEntity<?> getJuridicalAnswer(@PathVariable String inn) {
        JuridicalAnswer answer = adapterService.getJuridicalAnswer(inn);
        if(answer != null) {
            return ResponseEntity.ok(answer);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/answer/physical/{sts}")
    public ResponseEntity<?> getPhysicalAnswer(@PathVariable String sts) {
        PhysicalAnswer answer = adapterService.getPhysicalAnswer(sts);
        if(answer != null) {
            return ResponseEntity.ok(answer);
        }
        return ResponseEntity.badRequest().build();
    }
}
