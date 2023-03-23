package com.troian.bannerservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Value("${values.crud-permission-password}")
    private String crudPassword;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().contains("/bid")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null) {
            if(token.contains(crudPassword)) {
                return true;
            }
        }

        throw new AccessException("Method not allowed");
    }
}
