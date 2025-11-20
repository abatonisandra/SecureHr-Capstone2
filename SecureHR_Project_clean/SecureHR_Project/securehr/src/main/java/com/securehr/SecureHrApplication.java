package com.securehr;

import com.securehr.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SecureHrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureHrApplication.class, args);
    }
}
