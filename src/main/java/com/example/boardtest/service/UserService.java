package com.example.boardtest.service;

import com.example.boardtest.exception.user.UserAleradyExistsException;
import com.example.boardtest.exception.user.UserNotAllowedException;
import com.example.boardtest.exception.user.UserNotFoundException;
import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.user.User;
import com.example.boardtest.model.user.UserAuthenticationResponse;
import com.example.boardtest.model.user.UserPatchRequestBody;
import com.example.boardtest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


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

    //로그인
    public UserAuthenticationResponse login(String username, String password) {
        //저장된 유저 찾기
        var userEntity =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }


    //검색
    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;


        if (query != null && !query.isBlank()){
            //TODO: query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        }else{
            userEntities  = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from).toList();
    }

    //단건조회
    public User getUser(String username) {
        var userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    return User.from(userEntity);
    }

    //회원정보 수정

    public User updateUser(   String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var user =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        if (!currentUser.equals(user)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            user.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(user));
    }
}
