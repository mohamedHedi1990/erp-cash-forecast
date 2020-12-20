package org.apac.erp.cach.forecast.security;

import lombok.Data;

@Data
public class LoginRequest {
    private Long userId;

    private String username;

    private String userPassword;
}
