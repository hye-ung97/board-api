package org.board.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    final String schemeName = "authorization";
    return new OpenAPI()
        .info(new Info().title("Board API").version("v1"))
        .schemaRequirement(
            schemeName,
            new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"))
        .addSecurityItem(new SecurityRequirement().addList(schemeName));
  }
}
