package com.example.boardtest.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    //JWT 서명에 사용할 비밀 키를 생성
    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    //JWT 액세스 토큰에서 사용자 이름(주체)을 추출하는 공용 메서드
    public String getUsername(String jwtToken) {
        return getSubject(jwtToken);
    }

    //UserDetails에서 추출한 사용자 이름을 사용하여 JWT 액세스 토큰을 생성하는 공용 메서드
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    //jwt 토큰생성
    private String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + (1000 * 60 * 60 * 3));
        //issuedAt =발행시점 , expiration=만료시점
        return Jwts.builder().subject(subject).signWith(key).issuedAt(now).expiration(exp).compact();
    }

    //subject를 추출
    private String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            logger.error("JwtException", e);
            throw e;
        }
    }

}
