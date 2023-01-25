package by.x1ss.adapterservice;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LinksToOtherService.class)
@ConfigurationPropertiesScan
public class AdapterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdapterServiceApplication.class, args);
    }

}
