package com.putraawali.auth.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String issuer;
	private TokenProperties accessToken;
	private TokenProperties refreshToken;
	@Getter
	@Setter
	public static class TokenProperties {

		private String secretKey;

        private long expirationMinutes;
	}
}
