package com.example.boardtest.service;

import com.example.boardtest.exception.follow.FollowAlreadyExistsException;
import com.example.boardtest.exception.follow.FollowNotFoundException;
import com.example.boardtest.exception.follow.InvalidFollowException;
import com.example.boardtest.exception.user.UserAleradyExistsException;
import com.example.boardtest.exception.user.UserNotAllowedException;
import com.example.boardtest.exception.user.UserNotFoundException;
import com.example.boardtest.model.entity.FollowEntity;
import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.user.User;
import com.example.boardtest.model.user.UserAuthenticationResponse;
import com.example.boardtest.model.user.UserPatchRequestBody;
import com.example.boardtest.repository.FollowEntityRepository;
import com.example.boardtest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FollowEntityRepository followEntityRepository;


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


    //Follow
    @Transactional
    public User follow(String username, UserEntity currentUser) {
        //Follow 할려는 대상조회
        var following =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        //본인스스로 Follow 못하게 막는다
        if (currentUser.equals(following)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        followEntityRepository
                .findByFollowerAndFollowing(currentUser, following)
                .ifPresent(//해당 Follow가 존재하면 execption처리
                        follow -> {
                            throw new FollowAlreadyExistsException(currentUser, following);
                        });
        followEntityRepository.save(FollowEntity.of(currentUser, following));

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(currentUser.getFollowingsCount() + 1);

        userEntityRepository.save(following);
        userEntityRepository.save(currentUser);

        return User.from(following);
    }

    //Follow 취소
    @Transactional
    public User unFollow(String username, UserEntity currentUser) {
        var following =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        if (currentUser.equals(following)) {
            throw new InvalidFollowException("A user cannot unfollow themselves.");
        }

        var follow =
                followEntityRepository
                        .findByFollowerAndFollowing(currentUser, following)
                        .orElseThrow(() -> new FollowNotFoundException(currentUser, following));
        followEntityRepository.delete(follow);

        long newFollowersCount = Math.max(0, following.getFollowersCount() - 1);
        long newFollowingsCount = Math.max(0, currentUser.getFollowingsCount() - 1);
        following.setFollowersCount(newFollowersCount);
        currentUser.setFollowingsCount(newFollowingsCount);

        userEntityRepository.save(following);
        userEntityRepository.save(currentUser);

        return User.from(following);
    }

    //Followers 목록
    public List<User> getFollowersByUsername(String username) {
        var following =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        var followEntities = followEntityRepository.findByFollowing(following);
        return followEntities.stream().map(follow -> User.from(follow.getFollower())).toList();
    }


    //Followings 목록
    public List<User> getFollowingsByUsername(String username) {
        var follower =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        var followEntities = followEntityRepository.findByFollower(follower);
        return followEntities.stream().map(follow -> User.from(follow.getFollowing())).toList();
    }

}
