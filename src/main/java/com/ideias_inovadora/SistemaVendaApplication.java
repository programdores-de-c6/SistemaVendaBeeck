package com.ideias_inovadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;
@RestController
@EnableCaching
@SpringBootApplication
public class SistemaVendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaVendaApplication.class, args);
	}

}
