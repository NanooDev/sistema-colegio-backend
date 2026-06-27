package com.duoc.servicio_finanzas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioFinanzasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioFinanzasApplication.class, args);
    }
}
