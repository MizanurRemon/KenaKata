package org.kenakata;


import org.kenakata.Controller.APIController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args); //for running spring server
        System.out.println("everything ok");

    }
}