package com.hr.examportal.config;

import com.hr.examportal.filter.UserInfoFilter;
import com.hr.examportal.security.CookieBearerTokenResolver;
import com.hr.examportal.security.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserInfoFilter userInfoFilter;
    private final JwtAuthConverter jwtAuthConverter;
    private final CookieBearerTokenResolver cookieBearerTokenResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs (optional in prod)
                .authorizeHttpRequests(auth -> auth
                        // Open endpoints (no token required)
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // everything else needs a valid JWT from Keycloak
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(cookieBearerTokenResolver)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );
//                .addFilterAfter(userInfoFilter, SecurityContextPersistenceFilter.class);

        return http.build();
    }
}
