package com.duoc.servicio_cursos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("La suite de esta etapa usa tests unitarios de servicio, no contexto completo con DB")
class ServicioCursosApplicationTests {

	@Test
	void contextLoads() {
	}

}
