package com.xenoblade.filter;

import com.xenoblade.util.JwtUtils;
import io.jsonwebtoken.Claims;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.crypto.SecretKey;
import java.util.Set;

public class JwtFilter implements Filter {

    private final SecretKey jwtKey;

    public JwtFilter(SecretKey jwtKey) {
        this.jwtKey = jwtKey;
    }

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/auth/login",
            "/auth/register",
            "/auth/test"
            // Add more here
    );

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JwtFilter triggered for: " + ((HttpServletRequest) req).getRequestURI());

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        try {
            Claims claims = JwtUtils.validateToken(token, jwtKey);
            // optionally: request.setAttribute("userId", claims.getSubject());
            chain.doFilter(req, res);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }
}
