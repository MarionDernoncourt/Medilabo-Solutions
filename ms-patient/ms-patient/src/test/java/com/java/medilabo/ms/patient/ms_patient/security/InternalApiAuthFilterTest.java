package com.java.medilabo.ms.patient.ms_patient.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InternalApiAuthFilterTest {
    private InternalApiAuthFilter filter;
    private final String HEADER_NAME = "X-Internal-Secret";
    private final String SECRET_VALUE = "super-secret-key";

    @BeforeEach
    void setUp() {
        // On réinitialise le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
        filter = new InternalApiAuthFilter(HEADER_NAME, SECRET_VALUE);
    }

    @Test
    void doFilterInternal_WithCorrectSecret_ShouldAuthenticate() throws Exception {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HEADER_NAME, SECRET_VALUE);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // WHEN
        filter.doFilterInternal(request, response, filterChain);

        // THEN
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("internal-gateway", auth.getName());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GATEWAY")));

        // Vérifie que le filtre a bien laissé passer la requête
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithWrongSecret_ShouldReturn403() throws Exception {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HEADER_NAME, "wrong-key");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // WHEN
        filter.doFilterInternal(request, response, filterChain);

        // THEN
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Vérifie que la requête a été stoppée (doFilter JAMAIS appelé)
        verify(filterChain, never()).doFilter(request, response);
    }
}
