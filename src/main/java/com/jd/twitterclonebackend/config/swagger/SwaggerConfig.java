package com.jd.twitterclonebackend.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/*
    ACCESS THROUGH: http://localhost:8080/api/v1/swagger-ui/index.html
    Show only controllers with annotation @ApiRestController
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(UsernamePasswordAuthenticationToken.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("^(?!/(error).*$).*$"))
                .apis(RequestHandlerSelectors.withClassAnnotation(ApiRestController.class))
                .build()
                .securitySchemes(Collections.singletonList(createSchema()))
                .securityContexts(Collections.singletonList(createContext()))
                .apiInfo(getApiInfo());
    }

    private SecurityScheme createSchema() {
        return new ApiKey(
                "apiKey",
                "Authorization",
                "header"
        );
    }

    private SecurityContext createContext() {
        return SecurityContext.builder()
                .securityReferences(createSecurityReferences())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> createSecurityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global",
                "accessEverything"
        );

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Collections.singletonList(new SecurityReference(
                "apiKey",
                authorizationScopes
        ));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Twitter Clone API")
                .version("1.0")
                .description("Twitter Clone API")
                .contact(new Contact(
                        "Jonasz Delimata",
                        "https://github.com/poik12/",
                        "jonasz.dl@gmail.com"
                ))
                .license("Apache License Version 2.0")
                .build();
    }

}
