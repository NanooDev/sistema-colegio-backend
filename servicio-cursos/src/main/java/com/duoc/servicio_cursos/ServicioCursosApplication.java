package com.duoc.servicio_cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.cloud.openfeign.EnableFeignClients
public class ServicioCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioCursosApplication.class, args);
	}

}
