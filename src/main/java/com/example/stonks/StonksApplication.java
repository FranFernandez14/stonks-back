package com.example.stonks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StonksApplication {

	public static void main(String[] args) {

		SpringApplication.run(StonksApplication.class, args);
		System.out.println("Stonks se ha iniciado correctamente");
	}

}
