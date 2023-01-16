package by.x1ss.adapterservice.model.answer;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PhysicalAnswer {
    private String sts;
    private double accrualAmount;
    private double amountPay;
    private double resolutionNumber;
    private LocalDate resolutionDate;
    private String administrativeCode;
}

