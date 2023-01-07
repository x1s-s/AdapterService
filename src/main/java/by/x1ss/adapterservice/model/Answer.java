package by.x1ss.adapterservice.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Answer {
    private double accrualAmount;
    private double amountToPaid;
    private String resolutionNumber;
    private String vehicleCertificate;
    private LocalDate resolutionDate;
    private String articleAdministrativeCode;
}
