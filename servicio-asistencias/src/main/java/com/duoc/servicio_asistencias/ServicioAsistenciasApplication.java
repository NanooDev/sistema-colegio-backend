package com.duoc.servicio_asistencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioAsistenciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioAsistenciasApplication.class, args);
    }
}
