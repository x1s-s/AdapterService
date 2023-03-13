package by.x1ss.adapterservice.web.dto;

import by.x1ss.adapterservice.domain.object.Response;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseDTO {
    private String clientIdentifier;
    private double accrualAmount;
    private double amountPay;
    private double resolutionNumber;
    private LocalDate resolutionDate;
    private String administrativeCode;

    public ResponseDTO(Response response){
        this.clientIdentifier = response.getClientIdentifier();
        this.accrualAmount = response.getAccrualAmount();
        this.amountPay = response.getAmountPay();
        this.resolutionNumber = response.getResolutionNumber();
        this.resolutionDate = response.getResolutionDate();
        this.administrativeCode = response.getAdministrativeCode();
    }
}
