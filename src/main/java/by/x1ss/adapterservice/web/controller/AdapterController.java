package by.x1ss.adapterservice.web.controller;

import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.api.ServiceControllerAPI;
import by.x1ss.adapterservice.web.dto.ResponseListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "User", description = "Adapter service contoller for another service")
@Slf4j
@RequestMapping(path = "/adapter")
public class AdapterController {
    private final ServiceControllerAPI serviceControllerAPI;

    public AdapterController(ServiceControllerAPI serviceControllerAPI) {
        this.serviceControllerAPI = serviceControllerAPI;
    }

    @Operation(summary = "Put juridical request to query", tags = "request")
    @PostMapping("/answer/juridical")
    @ResponseStatus(HttpStatus.OK)
    public ResponseListDTO getJuridicalAnswer(@RequestBody String inn) {
        return new ResponseListDTO(serviceControllerAPI.getAnswer(inn, true));
    }

    @Operation(summary = "Put physical request to query", tags = "request")
    @PostMapping("/answer/physical")
    @ResponseStatus(HttpStatus.OK)
    public ResponseListDTO getPhysicalAnswer(@RequestBody String sts) {
        return new ResponseListDTO(serviceControllerAPI.getAnswer(sts, false));
    }
}
