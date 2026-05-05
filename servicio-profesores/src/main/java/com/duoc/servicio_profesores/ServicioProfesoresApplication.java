package com.duoc.servicio_profesores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioProfesoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioProfesoresApplication.class, args);
	}

}