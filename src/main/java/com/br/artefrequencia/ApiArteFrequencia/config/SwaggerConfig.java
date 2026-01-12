package com.br.artefrequencia.ApiArteFrequencia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI arteFrequenciaOpenAPI() {

                Info info = new Info();
                info.setTitle("API Arte Frequência");
                info.setVersion("1.0.0");
                info.setDescription("""
                                Documentação da API do projeto Arte Frequência

                                Desenvolvido por:
                                Equipe Arte & Frequência - ETE Jurandir Bezerra Lins
                                Email: piartefrequencia@gmail.com
                                GitHub: https://github.com/PIArteFrequencia
                                """);

                return new OpenAPI().info(info);
        }
}
