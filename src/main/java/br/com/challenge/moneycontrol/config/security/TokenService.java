package br.com.challenge.moneycontrol.config.security;

import br.com.challenge.moneycontrol.model.UserAccount;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    @Value(value = "${moneycontrol.jwt.expiration}")
    private String expiration;

    @Value(value = "${moneycontrol.jwt.secret}")
    private String secret;


    public String generateToken(Authentication authentication){
        UserAccount userActive = (UserAccount) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate =
                new Date(today.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("API moneyControl")
                .setSubject(userActive.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean isTokenValid(String token) {

        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims =
                Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
