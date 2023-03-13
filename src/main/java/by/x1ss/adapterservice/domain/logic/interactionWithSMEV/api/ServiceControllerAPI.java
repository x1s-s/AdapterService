package by.x1ss.adapterservice.domain.logic.interactionWithSMEV.api;

import by.x1ss.adapterservice.domain.object.ResponseList;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;


@Validated
public interface ServiceControllerAPI {
    ResponseList getAnswer(
            @Pattern(regexp = "^[\\d+]{10}$|^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
                    message = "Incorrect client identifier in request") String clientIdentifier
            , Boolean isJuridical);
}
