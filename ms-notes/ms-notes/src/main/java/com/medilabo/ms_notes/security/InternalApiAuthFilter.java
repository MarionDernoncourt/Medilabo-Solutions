package com.medilabo.ms_notes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class InternalApiAuthFilter extends OncePerRequestFilter {

    private final String headerName;
    private final String secretValue;

    public InternalApiAuthFilter(String headerName, String secretValue) {
        this.headerName = headerName;
        this.secretValue = secretValue;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestSecret = request.getHeader(headerName);
        if (secretValue != null && secretValue.equals(requestSecret)) {
            // ✅ C'est la Gateway : création d'une identité avec le rôle ROLE_INTERNAL
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "internal-gateway", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GATEWAY")));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // On laisse passer la requête vers le controller
            filterChain.doFilter(request, response);
        } else {
            // ❌ Ce n'est pas la Gateway ou le secret est faux : ON BLOQUE
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Acces direct non autorise. Vous devez passer par la Gateway.\"}");
        }
    }
}
