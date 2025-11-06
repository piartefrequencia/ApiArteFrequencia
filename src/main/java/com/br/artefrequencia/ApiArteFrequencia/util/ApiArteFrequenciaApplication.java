package com.br.artefrequencia.ApiArteFrequencia.util;


import org.springframework.boot.SpringApplication;


import io.github.cdimascio.dotenv.Dotenv;

public class ApiArteFrequenciaApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();

            String[] vars = {
                "DB_HOST",
                "DB_PORT",
                "DB_NAME",
                "DB_USER",
                "DB_PASSWORD",
                "CORS_ORIGIN",
                "API_BASE_PATH"
            };

            for (String var : vars) {
                String value = dotenv.get(var);
                if (value != null && !value.isEmpty()) {
                    System.setProperty(var, value);
                } else {
                    System.out.println("⚠️ Variável de ambiente '" + var + "' não encontrada no arquivo .env");
                }
            }

            SpringApplication.run(ApiArteFrequenciaApplication.class, args);
            System.out.println("✅ API ArteFrequencia iniciada com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar variáveis de ambiente ou iniciar a aplicação:");
            e.printStackTrace();
        }
    }
    
}
