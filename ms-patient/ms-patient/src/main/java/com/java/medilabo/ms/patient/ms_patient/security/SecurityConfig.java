package com.java.medilabo.ms.patient.ms_patient.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration de sécurité temporaire pour le développement.
 * DÉSACTIVE la protection CSRF et autorise toutes les requêtes (GET, POST, PUT, DELETE)
 * vers tous les endpoints sans authentification.
 * <p>
 * ATTENTION: Cette configuration NE DOIT PAS être utilisée en production.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${medilabo.security.internal-header}")
    private String headerName;
    @Value("${medilabo.security.internal-secret}")
    private String secretValue;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF (nécessaire pour les requêtes POST/PUT/DELETE sans token)
                .csrf(AbstractHttpConfigurer::disable)
                // Ajout du filtre de
                .addFilterBefore(new InternalApiAuthFilter(headerName, secretValue), UsernamePasswordAuthenticationFilter.class)
                // Configure les règles d'autorisation
                .authorizeHttpRequests(authorize -> authorize
                        // Autorise toutes les requêtes entrantes sans authentification
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

}