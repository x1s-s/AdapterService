package by.x1ss.adapterservice.model.answer;


import lombok.Data;

import java.time.LocalDate;

@Data
public class JuridicalAnswer{
    private String inn;
    private double accrualAmount;
    private double amountPay;
    private double resolutionNumber;
    private LocalDate resolutionDate;
    private String administrativeCode;
}

