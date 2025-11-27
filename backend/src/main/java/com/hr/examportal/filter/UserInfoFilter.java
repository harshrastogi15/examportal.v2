package com.hr.examportal.filter;


import com.hr.examportal.filter.entity.CustomUserPrincipal;
import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

//@Component
@RequiredArgsConstructor
public class UserInfoFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("In Filter, ");
        System.out.println("DEPRICATED FILTER");
        String email = "john.doe@example.com";
        User user = userRepository.findByEmail(email);

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


