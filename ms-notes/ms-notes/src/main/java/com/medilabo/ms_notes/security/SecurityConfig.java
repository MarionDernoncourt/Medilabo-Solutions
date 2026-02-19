package com.medilabo.ms_notes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("!test")
public class SecurityConfig {

    @Value("${medilabo.security.internal-header}")
    private String headerName;
    @Value("${medilabo.security.internal-secret}")
    private String secretValue;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF (microservice sans session)
                .csrf(AbstractHttpConfigurer::disable)
                // Ajout du filtre "douane" : verification badge gateway
                .addFilterBefore(new InternalApiAuthFilter(headerName, secretValue), UsernamePasswordAuthenticationFilter.class)
                // Configure les règles d'autorisation
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole("GATEWAY")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
