package com.java.medilabo.ms.patient.ms_patient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration de sécurité temporaire pour le développement.
 * DÉSACTIVE la protection CSRF et autorise toutes les requêtes (GET, POST, PUT, DELETE)
 * vers tous les endpoints sans authentification.
 *
 * ATTENTION: Cette configuration NE DOIT PAS être utilisée en production.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactive la protection CSRF (nécessaire pour les requêtes POST/PUT/DELETE sans token)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure les règles d'autorisation
                .authorizeHttpRequests(authorize -> authorize
                        // Autorise toutes les requêtes entrantes sans authentification
                        .anyRequest().permitAll()
                )
                // Active la configuration par défaut (utilisé ici pour des raisons de syntaxe moderne)
                .httpBasic(withDefaults());

        return http.build();
    }
}