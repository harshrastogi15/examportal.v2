package com.hr.examportal.utils;

import com.hr.examportal.filter.entity.CustomUserPrincipal;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserId {
    public UUID getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        return customUserPrincipal.getId();
    }
}
