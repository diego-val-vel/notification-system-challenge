package com.notifications.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for HTTP cross-origin access.
 *
 * <p>This configuration allows the React frontend running through Vite to call
 * the Spring Boot backend during local Docker-based execution.</p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures allowed origins, methods, and headers for API endpoints.
     *
     * @param registry CORS registry used by Spring MVC
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5174")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
