package com.duoc.servicio_calificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioCalificacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioCalificacionesApplication.class, args);
	}

}
