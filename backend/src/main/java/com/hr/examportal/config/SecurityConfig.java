package com.hr.examportal.config;

import com.hr.examportal.filter.UserInfoFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
public class SecurityConfig {

    private final UserInfoFilter userInfoFilter;

    public SecurityConfig(UserInfoFilter userInfoFilter) {
        this.userInfoFilter = userInfoFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs (optional in prod)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .addFilterAfter(userInfoFilter, SecurityContextPersistenceFilter.class);

        return http.build();
    }
}
