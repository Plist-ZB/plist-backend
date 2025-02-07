package com.zerobase.plistbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlistBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlistBackendApplication.class, args);
    }

}
