package by.x1ss.adapterservice.model.request.requestImpl;

import by.x1ss.adapterservice.model.request.PersonRequest;
import by.x1ss.adapterservice.model.request.PersonType;
import lombok.Data;

@Data
public class RequestFromPhysical implements PersonRequest {
    private String vehicleCertificate;

    @Override
    public String getType() {
        return PersonType.PHYSICAL.getType();
    }

    @Override
    public String getValue() {
        return vehicleCertificate;
    }
}
