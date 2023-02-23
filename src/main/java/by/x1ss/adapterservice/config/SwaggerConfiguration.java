package by.x1ss.adapterservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenApi(@Value("${pom.version}") String version) {
        return new OpenAPI().info(new Info().title("Adapter service")
                        .version(version)
                        .description("Adapter microservice to get penalty from SMEV")
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .contact(new Contact().name("x1ss")
                                .email("x1ss@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                        .description("Dev service")));
    }
}
