package br.com.pontoceub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components().addSecuritySchemes("spring_oauth",
                        new SecurityScheme().type(SecurityScheme.Type.OAUTH2).description("Autenticação para API")
                                .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(
                                        "")
                                )))).security(Arrays.asList(new SecurityRequirement().addList("spring_oauth")))
                .info(new Info().title("Timesheet API")
                        .contact(new Contact().email("vitorjjhg@hotmail.com").name("Developer: Vitor Costa"))
                        .version("1.0"));
    }
}
