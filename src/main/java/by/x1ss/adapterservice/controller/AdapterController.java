package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.service.AdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/adapter")
public class AdapterController {
    @Autowired
    private AdapterService adapterService;

    @GetMapping("/answer/juridical/{inn}")
    public ResponseEntity<?> getJuridicalAnswer(@PathVariable
                                                    @Pattern(regexp = "^[\\d+]{10}$", message = "Incorrect inn")
                                                    String inn) {
        return ResponseEntity.ok(adapterService.getResponse(inn, true));
    }

    @GetMapping("/answer/physical/{sts}")
    public ResponseEntity<?> getPhysicalAnswer(@PathVariable
                                                   @Pattern(regexp = "^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
                                                           message = "Incorrect sts")
                                                   String sts) {
        return ResponseEntity.ok(adapterService.getResponse(sts, false));
    }
}
