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

		// DB1
        System.setProperty("DB1_URL", dotenv.get("DB1_URL", System.getenv("DB1_URL")));
        System.setProperty("DB1_USER", dotenv.get("DB1_USER", System.getenv("DB1_USER")));
        System.setProperty("DB1_PASS", dotenv.get("DB1_PASS", System.getenv("DB1_PASS")));

        // DB2
        System.setProperty("DB2_URL", dotenv.get("DB2_URL", System.getenv("DB2_URL")));
        System.setProperty("DB2_USER", dotenv.get("DB2_USER", System.getenv("DB2_USER")));
        System.setProperty("DB2_PASS", dotenv.get("DB2_PASS", System.getenv("DB2_PASS")));

		System.setProperty("CORS_ORIGIN", dotenv.get("CORS_ORIGIN", System.getenv("CORS_ORIGIN")));
		
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET")));
		
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION")));

		SpringApplication.run(ApiArteFrequenciaApplication.class, args);

	}

}
