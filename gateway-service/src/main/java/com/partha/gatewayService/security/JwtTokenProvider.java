package com.partha.gatewayService.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider  {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	public String generateToken(Authentication authentication) {

		//UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		UserDetails userPrincipal=  (UserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
				//.setSubject(Long.toString(userPrincipal.getId()))
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	//public Long getUserIdFromJWT(String token) {
	//    Claims claims = Jwts.parser()
	//            .setSigningKey(jwtSecret)
	//            .parseClaimsJws(token)
	//            .getBody();
	//
	//    return Long.parseLong(claims.getSubject());
	//}


	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}


	public boolean validateToken(String authToken) {
		try {
			Jws<Claims> claim = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

			//the below if condition is added by me start here
			//additionally one can have custom algorithm here to check for some 
			//other validity condition of the token
			if(AuthenticatedTokenInventory.whiteListedTokens.contains(authToken))	            
				//additional condition for validity of token end here

				return true;
			//    } catch (SignatureException ex) {
			//        logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}

}

