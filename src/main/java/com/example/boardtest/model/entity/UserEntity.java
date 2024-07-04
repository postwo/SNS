package com.example.boardtest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Random;

@Entity
@Table(name = "\"user\"") //이렇게 하는이유는 postgres sql을 사용하고 있는데 postgres sql내부에서 이미 사용되고 있는 예약어 이다 그래서 이렇게 해줘야 데이터베이스 내부적으로 사용가능하다
@Getter
@Setter
//이렇게 하면 서비스상에는 존재하지 않지만 데이터베이스 상에는 내부적으로 존재하게 삭제할수 있다
//id 기준으로 데이터베이스에서 삭제하는게 deleteddatetime을 현재시간으로 수정하게 한거다
@SQLDelete(sql="update \"user\" set deletedDateTime = CURRENT_TIMESTAMP where userid = ?")
//삭제 되지 않은 게시물 보기 위해 사용
@SQLRestriction("deleteddatetime IS NULL")
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String profile;

    @Column
    private String description;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserEntity of(String username,String password){
        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        //Avatar placeholder 서비스(https://avatar-placeholder.iran.liara.run) 기반
        // 랜덤한 프로필 사진 설정(1 ~ 100) == 랜덤한 프로필이미지 주소 활용
        userEntity.setProfile("https://avatar.iran.liara.run/public/"+ new Random().nextInt(100));

        //위 api가 정상적으로 동작하지 않을 경우, 이것을 사용한다
//        userEntity.setProfile(
//                "https://dev-jayce.github.io/public/profile/"+ new Random().nextInt(100)+".png");

        return userEntity;
    }

    //밑의 어노테이션들은 jpa에 의해서 실제 데이터가 내부적으로 저장되기 직전에 혹은 수정되기 직전에 원하는 로직을 수행할수있다
    @PrePersist
    private void prePersist(){
        this.createdDateTime =ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedDateTime = ZonedDateTime.now();
    }
}
