package com.blu.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.blu.*"})
@EntityScan("com.blu.*")
@EnableJpaRepositories("com.blu.auth")
public class BluBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BluBackendApplication.class, args);
    }

}
