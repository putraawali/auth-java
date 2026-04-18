package com.putraawali.auth.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    final private String issuer = "putraawali.com";
	private String secretKey;
	private long accessTokenExpiration;
	private long refreshTokenExpiration;
}
