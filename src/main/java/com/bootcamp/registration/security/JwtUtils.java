package com.bootcamp.registration.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private int jwtExpirationInMs;

    public String generateJwtToken(Authentication authentication){
        final Date createdDate = new Date();
        final Date expiryDate = new Date(createdDate.getTime()+jwtExpirationInMs);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(createdDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String getUserNameFromJwtToken(String token){

        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return Boolean.TRUE;
        }catch(SignatureException signatureException){
            logger.error("Invalid Jwt Signature: {}",signatureException.getMessage());
        }catch(MalformedJwtException malformedJwtException){
            logger.error("Invalid Jwt Token: {}",malformedJwtException.getMessage());
        }catch(ExpiredJwtException expiredJwtException){
            logger.error("Jwt Token is expired :{}", expiredJwtException.getMessage());
        }catch(UnsupportedJwtException unsupportedJwtException){
            logger.error("Jwt Token is unsupported :{}", unsupportedJwtException.getMessage());
        }catch(IllegalArgumentException illegalArgumentException){
            logger.error("Jwt Claim string is empty: {}",illegalArgumentException.getMessage());
        }
        return false;
    }
}
