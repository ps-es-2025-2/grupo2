package br.com.geb.api.config;

import br.com.geb.api.enums.Papel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties props;

    private Key key(){
        return Keys.hmacShaKeyFor(props.getSecret().getBytes());
    }

    public String generateToken(String email, Papel papel){
        Date now = new Date();
        Date exp = new Date(now.getTime() + props.getExpirationMs());
        return Jwts.builder()
                .setSubject(email)
                .claim("role", papel.name())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
    }

    public String getEmail(String token){
        return parse(token)
                .getBody()
                .getSubject();
    }
}
