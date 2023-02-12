package by.x1ss.adapterservice.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.link")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinksToOtherService {
    private String request;
    private String getAnswer;
    private String confirm;
    private String status;
}
