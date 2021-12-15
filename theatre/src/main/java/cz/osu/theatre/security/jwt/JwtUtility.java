package cz.osu.theatre.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtUtility {
    @Value("${auth.jwtSecret}")
    private String secret_JWT;

    @Value("${auth.jwtAccessTokenExpirationMs}")
    private int jwtAccessTokenExpiration;

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtAccessTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, secret_JWT)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret_JWT)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret_JWT).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error(String.format("Invalid JWT signature: %s", e.getMessage()));
        } catch (MalformedJwtException e) {
            log.error(String.format("Invalid JWT token: %s", e.getMessage()));
        } catch (ExpiredJwtException e) {
            log.error(String.format("JWT token is expired: %s", e.getMessage()));
        } catch (UnsupportedJwtException e) {
            log.error(String.format("JWT token is unsupported: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error(String.format("JWT claims string is empty: %s", e.getMessage()));
        }

        return false;
    }
}