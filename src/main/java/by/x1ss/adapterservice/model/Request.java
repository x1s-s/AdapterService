package by.x1ss.adapterservice.model;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Request {
    private final UUID uuid;
    private final String clientIdentifier;
    private final Boolean isJuridical;

    public Request(String clientIdentifier, Boolean isJuridical) {
        this.clientIdentifier = clientIdentifier;
        this.uuid = UUID.randomUUID();
        this.isJuridical = isJuridical;
    }
}
