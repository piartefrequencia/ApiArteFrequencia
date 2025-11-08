package com.br.artefrequencia.ApiArteFrequencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import io.github.cdimascio.dotenv.Dotenv;



@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ApiArteFrequenciaApplication {

	public static void main(String[] args) {

			// Carrega variáveis do .env se existir localmente
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // ignora se não existir (ex: em produção no Railway)
				.load();

		// Seta propriedades de sistema
		System.setProperty("DB_HOST", dotenv.get("DB_HOST", System.getenv("DB_HOST")));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT", System.getenv("DB_PORT")));
		System.setProperty("DB_USER", dotenv.get("DB_USER", System.getenv("DB_USER")));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", System.getenv("DB_PASSWORD")));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME", System.getenv("DB_NAME")));

		
		SpringApplication.run(ApiArteFrequenciaApplication.class, args);

	}

}
