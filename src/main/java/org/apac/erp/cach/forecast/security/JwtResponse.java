package org.apac.erp.cach.forecast.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JwtResponse {
    private String jwt;

    private Long id;

    private String username;


    private List<String> roles ;

}
