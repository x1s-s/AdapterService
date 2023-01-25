package by.x1ss.adapterservice.model.request;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Request {
    private final UUID uuid;
    private final String str;
    private final Boolean isJuridical;

    public Request(String str, Boolean isJuridical) {
        this.str = str;
        this.uuid = UUID.randomUUID();
        this.isJuridical = isJuridical;
    }
}
