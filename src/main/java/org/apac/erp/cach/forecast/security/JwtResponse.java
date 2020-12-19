package org.apac.erp.cach.forecast.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String token;

	private Long id;

	private String username;

	private List<String> roles;

}
