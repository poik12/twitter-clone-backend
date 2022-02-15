package com.jd.twitterclonebackend.configuration;

import com.jd.twitterclonebackend.repository.UserRepository;
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/swagger-ui.html",
//                        "/v2/api-docs",
//                        "/webjars/**",
//                        "/swagger-resources/**").permitAll()
//                .antMatchers("/api/user/**").permitAll()
//                .antMatchers("/api/login/**", "/api/token/refresh/**").permitAll()
//                .anyRequest().authenticated();


        // Stateless Session because of JWT
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Swagger API
        httpSecurity.authorizeRequests().antMatchers(
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**",
                "/swagger-resources/**").permitAll();

        // H2 Database
        httpSecurity.authorizeRequests().antMatchers("h2-console/**").permitAll();

        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/auth/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/users/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(API_VERSION + "/login/**").permitAll();
//        httpSecurity.authorizeRequests().antMatchers("/api/login/**", "/api/user/token/refresh").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/api/post/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/api/comment/**").permitAll();
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
