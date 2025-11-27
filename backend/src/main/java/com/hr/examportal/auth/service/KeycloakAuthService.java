package com.hr.examportal.auth.service;

import com.hr.examportal.auth.dto.TokenResponse;
import com.hr.examportal.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.redirect-uri}")
    private String redirectUri;


    public TokenResponse exchangeCodeForTokens(String code) {
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(form, headers);

//        ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, TokenResponse.class);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, TokenResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();
            throw new CustomException("NOT AUTHENTICATED", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            throw new CustomException("Unexpected error", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public TokenResponse refreshTokens(String refreshToken) {
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, TokenResponse.class);
        return response.getBody();
    }
}
