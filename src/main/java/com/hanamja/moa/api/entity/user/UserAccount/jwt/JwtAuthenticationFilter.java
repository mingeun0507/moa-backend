package com.hanamja.moa.api.entity.user.UserAccount.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.MessageInterpolator;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenUtil.resolveToken(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(token, "");

        Authentication authenticatedAuthentication = authenticationManager.authenticate(authentication);
        if (authenticatedAuthentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedAuthentication);
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
        }
    }
}

//public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    public JwtAuthenticationFilter(String defaultFilterProcessesUrl) {
//        super(defaultFilterProcessesUrl);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String token = jwtTokenUtil.resolveToken(request);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(token, "");
//
//        return getAuthenticationManager()
//                .authenticate(new UsernamePasswordAuthenticationToken(authentication, ""));
//
//    }
//
//
//    protected AuthenticationManager getAuthenticationManager() {
//        return super.getAuthenticationManager();
//    }
//}