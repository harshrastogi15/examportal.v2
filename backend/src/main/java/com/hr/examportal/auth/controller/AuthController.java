package com.hr.examportal.auth.controller;

import com.hr.examportal.auth.dto.TokenResponse;
import com.hr.examportal.auth.service.KeycloakAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;


    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("OneTime ")) {
            return ResponseEntity.badRequest().build();
        }
        String code = authHeader.substring("OneTime ".length()).trim();
        TokenResponse tokens = keycloakAuthService.exchangeCodeForTokens(code);

        if (tokens == null || tokens.getAccess_token() == null) {
            return ResponseEntity.status(502).build();
        }

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", tokens.getAccess_token())
                .httpOnly(true)
                .secure(false) // set true in prod
                .path("/")
                .maxAge(tokens.getExpires_in())
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", tokens.getRefresh_token())
                .httpOnly(true)
                .secure(false) // set true in prod
                .path("/api/auth")
                .maxAge(tokens.getRefresh_expires_in())
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (var c : request.getCookies()) {
                if ("REFRESH_TOKEN".equals(c.getName())) {
                    refreshToken = c.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        TokenResponse tokens = keycloakAuthService.refreshTokens(refreshToken);
        if (tokens == null || tokens.getAccess_token() == null) {
            return ResponseEntity.status(502).build();
        }

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", tokens.getAccess_token())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(tokens.getExpires_in())
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", tokens.getRefresh_token())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(tokens.getRefresh_expires_in())
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
