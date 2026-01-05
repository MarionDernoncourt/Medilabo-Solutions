package com.medilabo.ms_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Désactivé pour les APIs
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()     // Oblige l'identification pour chaque requete
                )
                .httpBasic(Customizer.withDefaults()); // Active l'authentification Basic (Header Authorization)

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Création de l'utilisateur systeme
        UserDetails user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Standard actuel : BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Autorisation de l'application Vue
        config.addAllowedOrigin("http://localhost:5173");
        // Autorisation des methodes classiques
        config.addAllowedMethod("*");
        // Autorisation de tous les headers
        config.addAllowedHeader("*");
        // Autorisation d'envoyer des identifiants
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}