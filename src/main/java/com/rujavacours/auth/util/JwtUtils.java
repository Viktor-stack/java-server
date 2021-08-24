package com.rujavacours.auth.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rujavacours.auth.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.logging.Level;

@Component
@Log
public class JwtUtils {

    public static final String CLAIM_USER_KEY = "user";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private int accessTokenExpiration;

    @Value("${jwt.login-expiration}")
    private int resetPassTokenExpiration;

    public String createAccessToken(User user) {
        return createToken(user, accessTokenExpiration);
    }

    public String crateEmailResetToken(User user) {
        return createToken(user, resetPassTokenExpiration);
    }


    /**
     * Создание Jwt Token
     *
     * @param user User
     * @return Token String
     * @author Viktor Kuldorov
     */
    private String createToken(User user, int duration) {

        Date currentDate = new Date();
        user.setPassword(null);
        Map claims = new HashMap<String, Object>();
        claims.put(CLAIM_USER_KEY, user);
        claims.put(Claims.SUBJECT, user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + duration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid jwt token: ", jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT Token is expired");
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string empty");
        }
        return false;
    }

    public User getUser(String jwt) {
        Map map = (Map) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get(CLAIM_USER_KEY);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, User.class);
    }
}
