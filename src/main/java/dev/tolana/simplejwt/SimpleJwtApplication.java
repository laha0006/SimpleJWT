package dev.tolana.simplejwt;

import dev.tolana.simplejwt.login.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SimpleJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleJwtApplication.class, args);
    }

}
