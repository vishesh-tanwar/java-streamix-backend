package com.yt.backend.subscription.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yt.backend.subscription.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response, FilterChain chain)
    // throws IOException, ServletException {
    // String authHeader = request.getHeader("Authorization");

    // if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    // chain.doFilter(request, response);
    // return;
    // }

    // String token = authHeader.substring(7);

    // if (!jwtUtils.verifyToken(token)) {
    // chain.doFilter(request, response);
    // return;
    // }

    // String email = jwtUtils.getMail(token);
    // Long userId = jwtUtils.getUserId(token);

    // if (jwtUtils.verifyToken(token)) {
    // UsernamePasswordAuthenticationToken authToken = new
    // UsernamePasswordAuthenticationToken(
    // email, null);

    // authToken.setDetails(userId);

    // SecurityContextHolder.getContext().setAuthentication(authToken);
    // }
    // chain.doFilter(request, response);

    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("HEADER = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO BEARER HEADER");
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("TOKEN = " + token);

        Boolean valid = jwtUtils.verifyToken(token);
        System.out.println("VERIFY = " + valid);

        if (!valid) {
            System.out.println("TOKEN INVALID");
            chain.doFilter(request, response);
            return;
        }

        String email = jwtUtils.getMail(token);
        Long userId = jwtUtils.getUserId(token);

        System.out.println("EMAIL = " + email);
        System.out.println("USERID = " + userId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, null);

        authToken.setDetails(userId);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("AUTH SET");

        chain.doFilter(request, response);
    }

}
