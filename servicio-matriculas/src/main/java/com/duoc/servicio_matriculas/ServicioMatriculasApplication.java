package com.duoc.servicio_matriculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioMatriculasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioMatriculasApplication.class, args);
    }
}
