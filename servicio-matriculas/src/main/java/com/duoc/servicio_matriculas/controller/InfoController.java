package com.duoc.servicio_matriculas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class InfoController {

	@GetMapping("/info")
	public Map<String, String> info() {
		return Map.of(
				"servicio", "matriculas",
				"estado", "esqueleto",
				"descripcion", "Microservicio generado para el sistema colegio; sin integraciones aún."
		);
	}
}
