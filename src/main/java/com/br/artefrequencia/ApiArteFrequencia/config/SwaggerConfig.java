package com.br.artefrequencia.ApiArteFrequencia.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

      @Bean
    public OpenAPI arteFrequenciaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Arte Frequência")
                        .description("Documentação da API do projeto Arte Frequência")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Arte & Frequência ETE Jurandir Bezerra Lins ")
                                .email("piartefrequencia@gmail.com")
                                .url("https://github.com/PIArte&Frequência")));
    }
}

