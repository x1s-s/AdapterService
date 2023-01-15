package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.model.request.requestImpl.RequestFromJuridical;
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
        return ResponseEntity.ok(adapterService.getJuridicalAnswer(request));
    }

    @GetMapping("/answer/physical/")
    public ResponseEntity<?> getPhysicalAnswer(@RequestBody RequestFromJuridical request) {
        return ResponseEntity.ok(adapterService.getPhysicalAnswer(request));
    }
}
