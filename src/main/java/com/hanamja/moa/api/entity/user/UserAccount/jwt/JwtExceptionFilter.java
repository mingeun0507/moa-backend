package com.hanamja.moa.api.entity.user.UserAccount.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanamja.moa.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (CustomException e) {
            final Map<String, Object> body = new HashMap<>();
            final ObjectMapper mapper = new ObjectMapper();
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            body.put("message", e.getMessage());
            mapper.writeValue(response.getOutputStream(), body);
        }
    }
}
