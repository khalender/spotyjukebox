package com.nrlp.spotyjukebox.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nrlp.spotyjukebox.*"})
public class SpotyJukeBoxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpotyJukeBoxApplication.class, args);
    }
}