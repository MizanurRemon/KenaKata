package org.kenakata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSwagger2
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args); //for running spring server
        System.out.println("everything ok");

    }

    //main

   /* @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.kenakata")).build();
    }*/
}