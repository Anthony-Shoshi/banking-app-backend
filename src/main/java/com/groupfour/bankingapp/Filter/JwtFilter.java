package com.groupfour.bankingapp.Filter;


import com.groupfour.bankingapp.Security.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;

    public JwtFilter(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request, response, filterChain);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            PrintWriter writer = response.getWriter();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.write(e.getMessage());
            writer.flush();
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
