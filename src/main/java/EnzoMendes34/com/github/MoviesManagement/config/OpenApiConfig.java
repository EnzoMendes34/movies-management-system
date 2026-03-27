package EnzoMendes34.com.github.MoviesManagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de gestão de cinema")
                        .version("v1")
                        .description("API RESTful para gerenciamento de cinema, contemplando autentiação, catálogo de filmes, controle de sessões, gerenciamento " +
                                "de salas e assentos, reservas com controle de concorrência e integração de pagamentos via Stripe.")
                        .termsOfService("https://github.com/EnzoMendes34")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/EnzoMendes34/movies-management-system"))
                );
    }
}
