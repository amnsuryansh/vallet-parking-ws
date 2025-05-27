package com.project.valletparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.project.*"})
@EnableJpaRepositories(basePackages = {"com.project.*"})
public class ValletParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValletParkingApplication.class, args);
    }

}
