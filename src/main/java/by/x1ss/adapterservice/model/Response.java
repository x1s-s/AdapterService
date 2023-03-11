package by.x1ss.adapterservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Response {
    private String clientIdentifier;
    private double accrualAmount;
    private double amountPay;
    private double resolutionNumber;
    private LocalDate resolutionDate;
    private String administrativeCode;
    @JsonIgnore
    private Boolean isJuridical;
}
