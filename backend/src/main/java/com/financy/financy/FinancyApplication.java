package com.financy.financy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FinancyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancyApplication.class, args);
	}

}
