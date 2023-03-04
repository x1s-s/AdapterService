package by.x1ss.adapterservice.controller;

import by.x1ss.adapterservice.model.Response;
import by.x1ss.adapterservice.service.AdapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@Validated
@Tag(name = "User", description = "Adapter service contoller for another service")
@Slf4j
@RequestMapping(path = "/adapter", produces = "application/xml")
public class AdapterController {
    @Autowired
    private AdapterService adapterService;

    @Operation(summary = "Put juridical request to query", tags = "request")
    @GetMapping("/answer/juridical")
    public ResponseEntity<Response> getJuridicalAnswer(@RequestBody
                                                    @Pattern(regexp = "^[\\d+]{10}$", message = "Incorrect inn")
                                                    String inn) {
        log.info("inn : {}", inn);
        return ResponseEntity.ok(adapterService.getAnswer(inn, true));
    }

    @Operation(summary = "Put physical request to query", tags = "request")
    @GetMapping("/answer/physical")
    public ResponseEntity<Response> getPhysicalAnswer(@RequestBody
                                                   @Pattern(regexp = "^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
                                                           message = "Incorrect sts")
                                                   String sts) {
        return ResponseEntity.ok(adapterService.getAnswer(sts, false));
    }
}
