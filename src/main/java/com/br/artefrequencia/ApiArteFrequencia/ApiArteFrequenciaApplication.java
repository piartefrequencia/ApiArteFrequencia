package com.br.artefrequencia.ApiArteFrequencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ApiArteFrequenciaApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		System.setProperty("DB_HOST", dotenv.get("DB_HOST", System.getenv("DB_HOST")));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT", System.getenv("DB_PORT")));
		System.setProperty("DB_USER", dotenv.get("DB_USER", System.getenv("DB_USER")));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", System.getenv("DB_PASSWORD")));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME", System.getenv("DB_NAME")));
		System.setProperty("CORS_ORIGIN", dotenv.get("CORS_ORIGIN", System.getenv("CORS_ORIGIN")));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET")));
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION")));

		SpringApplication.run(ApiArteFrequenciaApplication.class, args);

	}

}
