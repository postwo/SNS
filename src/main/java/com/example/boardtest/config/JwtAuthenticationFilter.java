package com.example.boardtest.config;

import com.example.boardtest.exception.jwt.JwtTokenNotFoundException;
import com.example.boardtest.service.JwtService;
import com.example.boardtest.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    //클라이언트 요청이 처리되는 가정에서 필터가 중복해서 여러변 수행할수 있기 때문에 이렇게 한번만 수행하게 해준다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: JWT검증
        String BEARER_PERFIX = "BEARER ";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();//인증정보 설정


        // jwt키값을 안넣었을때 또는 BEARER_PERFIX가 아닐경우 예외처리
        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(BEARER_PERFIX)){
            throw new JwtTokenNotFoundException();
        }

        //jwt인증 수행 조건
        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith(BEARER_PERFIX) && securityContext.getAuthentication() ==null){
            var accessToken = authorization.substring(BEARER_PERFIX.length());
            var username = jwtService.getUsername(accessToken);
            var userDetails = userService.loadUserByUsername(username);

            var AuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails ,null, userDetails.getAuthorities()
            );

            AuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(AuthenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request,response);
    }
}
