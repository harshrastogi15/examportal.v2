package com.hr.examportal.security;

import com.hr.examportal.exception.CustomException;
import com.hr.examportal.filter.entity.CustomUserPrincipal;
import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    @NotNull
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // obtain email / username from token
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            email = jwt.getClaimAsString("preferred_username");
        }
        if (email == null || email.isBlank()) {
            email = jwt.getSubject();
        }

        Optional<User> maybeUser = Optional.empty();
        if (email != null && !email.isBlank()) {
            try {
                System.out.println(email);
                maybeUser = Optional.ofNullable(userRepository.findByEmail(email));
            } catch (Exception e) {
                maybeUser = Optional.empty();
            }
        }

        CustomUserPrincipal principal;
        Collection<SimpleGrantedAuthority> authorities;

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            principal = new CustomUserPrincipal(user.getId(), user.getEmail());
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
        } else {
            // fallback principal if not found — adjust to throw if you prefer strict behavior
            String name = (email != null) ? email : jwt.getSubject();
            principal = new CustomUserPrincipal(null, name);
            authorities = List.of(); // no roles
//            throw new CustomException("User not found",HttpStatus.NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, jwt, authorities);

        auth.setDetails(jwt.getClaims());
        return auth;
    }
}
