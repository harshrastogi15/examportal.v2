package com.hr.examportal.filter;


import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

@Component
@RequiredArgsConstructor
public class UserInfoFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("In Filter, ");

        String email = "john.doe@example.com";
        User user = userRepository.findByEmail(email);

        @Getter
        class CustomUserPrincipal {
            private UUID id;
            private String email;

            public CustomUserPrincipal(UUID id, String email) {
                this.id = id;
                this.email = email;
            }
        }

        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(user.getId(),user.getEmail());
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserPrincipal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request,response);
    }
}


//@Component
//public class UserInfoFilter extends OncePerRequestFilter {
//
//    private final UserDetailsService userDetailsService;
//
//    public UserInfoFilter(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // Check if authentication is already set by JWT validation
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            // Extract user email (or username) from authentication principal (depends on token claims)
//            String email = null;
//
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
//                email = ((org.springframework.security.oauth2.jwt.Jwt) principal).getClaimAsString("email");
//            } else if (principal instanceof String) {
//                email = (String) principal;  // Sometimes principal can be a String username
//            }
//
//            if (email != null) {
//                // Load user details from DB
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//                // Create new Authentication token with roles from DB userDetails
//                UsernamePasswordAuthenticationToken newAuth =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                // Replace authentication with enriched userDetails authentication
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

