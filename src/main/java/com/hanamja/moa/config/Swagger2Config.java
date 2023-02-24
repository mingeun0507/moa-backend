package com.hanamja.moa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi
                .builder()
                .group("moa-apis")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/auth/login")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement();
        return new OpenAPI()
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes("bearer-jwt", securityScheme)
                )
                .security(Arrays.asList(securityRequirement))
                .info(
                        new Info()
                                .title("MOA API")
                                .description("MOA의 API 명세서입니다.")
                                .version("v0.1.1")
                );
    }
}
