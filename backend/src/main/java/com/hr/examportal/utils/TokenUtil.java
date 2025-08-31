package com.hr.examportal.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes
    private final ObjectMapper objectMapper;

    public String generateToken(Object object) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(object);

        return Jwts.builder()
                .setSubject(jsonPayload)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
        } catch (Exception e) {
            throw new CustomException("Failed to generate token");
        }
    }

    public <T> T validateTokenAndGetObjectName(String token, Class<T> clazz) {
        try{
            String jsonPayload = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return objectMapper.readValue(jsonPayload, clazz);
        }catch (Exception e){
            throw new CustomException("Failed to validate token");
        }
    }
}

