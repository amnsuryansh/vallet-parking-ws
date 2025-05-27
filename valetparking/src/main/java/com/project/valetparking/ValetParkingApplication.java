package com.project.valetparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.project.*"})
@EnableJpaRepositories(basePackages = {"com.project.*"})
public class ValetParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValetParkingApplication.class, args);
    }

}
