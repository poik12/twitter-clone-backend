package com.jd.twitterclonebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
class WebConfig implements WebMvcConfigurer {

    public static final Long MAX_AGE = 3600L;
    public static final String FRONTEND = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry
                .addMapping("/**")
                .allowedOrigins(FRONTEND)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                )
                .allowedHeaders("*")
                .maxAge(MAX_AGE)
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.AUTHORIZATION)
                .allowCredentials(true);
    }

}
