package com.jd.twitterclonebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    public static final Long MAX_AGE = 3600L;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry
                .addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                )
                .allowedHeaders("*")
                .maxAge(MAX_AGE)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(false);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//
//        corsRegistry
//                .addMapping("/api/v1/**")
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods(
//                        HttpMethod.GET.name(),
//                        HttpMethod.POST.name(),
//                        HttpMethod.PUT.name(),
//                        HttpMethod.DELETE.name()
//                )
//                .allowedHeaders(
//                        HttpHeaders.AUTHORIZATION,
//                        HttpHeaders.CONTENT_TYPE,
//                        HttpHeaders.ACCEPT
//                )
//                .maxAge(MAX_AGE)
//                .exposedHeaders(HttpHeaders.AUTHORIZATION)
//                .allowCredentials(true);
//    }

}
