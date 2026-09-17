package com.ideias_inovadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
@RestController
@EnableCaching
@EnableScheduling
@EnableAsync
@SpringBootApplication
//@ConfigurationPropertiesScan("com.ideias_inovadora.config")
public class SistemaVendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaVendaApplication.class, args);
	}

}
