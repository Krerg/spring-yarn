package com.mylnikov.appmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class AppmasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppmasterApplication.class, args);
    }

}