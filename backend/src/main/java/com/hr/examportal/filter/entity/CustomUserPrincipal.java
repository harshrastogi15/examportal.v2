package com.hr.examportal.filter.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomUserPrincipal {
    private UUID id;
    private String email;

    public CustomUserPrincipal(UUID id, String email) {
        this.id = id;
        this.email = email;
    }
}
