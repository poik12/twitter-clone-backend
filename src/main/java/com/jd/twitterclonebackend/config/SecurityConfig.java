package com.jd.twitterclonebackend.config;

import com.jd.twitterclonebackend.security.filter.CustomAuthenticationFilter;
import com.jd.twitterclonebackend.security.filter.CustomAuthorizationFilter;
import com.jd.twitterclonebackend.security.jwt.AccessTokenProvider;
import com.jd.twitterclonebackend.security.jwt.RefreshTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public static final String API_VERSION = "/api/v1";

    // Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManagerBean(),
                accessTokenProvider,
                refreshTokenProvider
        );
        customAuthenticationFilter.setFilterProcessesUrl(API_VERSION + "/auth/login");

        CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter(accessTokenProvider);

        // Disable Cross-Site Request Forgery
        httpSecurity.csrf().disable();
        // Config Cross-Origin Resource Sharing in WebConfig Class
        httpSecurity.cors();
        // Stateless Session because of JWT
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Swagger API
        // ACCESS THROUGH: http://localhost:8080/swagger-ui/index.html
        httpSecurity.authorizeRequests()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/webjars/**")
                .permitAll();
        // H2 Database
        httpSecurity.authorizeRequests().antMatchers("h2-console/**").permitAll();
        // CONTROLLER MAPPINGS
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/auth/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/users/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/posts/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/comments/**").permitAll();
//        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
//        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.addFilter(customAuthenticationFilter);
        httpSecurity.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
