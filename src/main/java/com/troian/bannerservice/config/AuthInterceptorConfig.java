package com.troian.bannerservice.config;

import com.troian.bannerservice.security.AuthHandlerInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AuthInterceptorConfig implements WebMvcConfigurer {

    private final AuthHandlerInterceptor authHandlerInterceptor;

    public AuthInterceptorConfig(AuthHandlerInterceptor authHandlerInterceptor) {
        this.authHandlerInterceptor = authHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor);
    }
}
