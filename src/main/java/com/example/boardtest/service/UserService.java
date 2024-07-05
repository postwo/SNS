package com.example.boardtest.service;

import com.example.boardtest.exception.user.UserAleradyExistsException;
import com.example.boardtest.model.user.User;
import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(username));
    }

    //회원가입
    public User signUp(String username, String password) {
        //유저가 존재하는지 조회
          userEntityRepository.findByUsername(username).ifPresent(
                 user->{
                     throw new UserAleradyExistsException();
                 });

       var userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));
        return User.from(userEntity);
    }
}
