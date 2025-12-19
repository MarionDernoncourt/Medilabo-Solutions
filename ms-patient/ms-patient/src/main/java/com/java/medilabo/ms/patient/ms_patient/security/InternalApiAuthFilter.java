package com.java.medilabo.ms.patient.ms_patient.security;

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

        if (secretValue.equals(requestSecret)) {
            // Creation d'une identité "interne" pour Spring Security
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("internal-gateway", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_INTERNAL")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
