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

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    //username을 추출해 jwt토큰으로 생성
    public String genrateAccessToken(UserDetails userDetails){
        return genrateToken(userDetails.getUsername());
    }


    public String getUsername(String accessToken){
        return getSubject(accessToken);
    }


    //jwt 토큰생성
    private String genrateToken(String subject){
        //만료기한
        var now = new Date();
        var exp = new Date(now.getTime()+(1000* 60* 60 *3));

        return  Jwts.builder().subject(subject).signWith(key)
                .issuedAt(now) //발행 시점
                .expiration(exp) //만료 시점
                .compact();
    }

    //subject를 추출
    private String getSubject(String token){

        try{
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        }catch (JwtException e){
            logger.error("JwtException",e);
            throw e;
        }

    }

}
