package by.x1ss.adapterservice.model.request.requestImpl;

import by.x1ss.adapterservice.model.request.PersonRequest;
import by.x1ss.adapterservice.model.request.PersonType;
import lombok.Data;

@Data
public class RequestFromJuridical implements PersonRequest {
    private String taxpayerIdentificationNumber;

    @Override
    public String getType() {
        return PersonType.PHYSICAL.getType();
    }

    @Override
    public String getValue() {
        return taxpayerIdentificationNumber;
    }
}
