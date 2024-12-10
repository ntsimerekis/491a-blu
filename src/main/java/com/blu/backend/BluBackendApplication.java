package com.blu.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//scan for all packages
@SpringBootApplication(scanBasePackages = {"com.blu.*"})
//scan for all entities in all packages (maybe just set to com.blu.model?)
@EntityScan("com.blu.*")
//enable repository package
@EnableJpaRepositories("com.blu.repository")
public class BluBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BluBackendApplication.class, args);
    }

}
