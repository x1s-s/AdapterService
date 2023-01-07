package by.x1ss.adapterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AdapterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdapterServiceApplication.class, args);
    }

}
