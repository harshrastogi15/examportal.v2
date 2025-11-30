package com.hr.examportal.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_in")
    private long expires_in;
    @JsonProperty("refresh_expires_in")
    private long refresh_expires_in;
    private String scope;
    @JsonProperty("id_token")
    private String id_token;
}
