package com.na.silserver.domain.user.entity;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.enums.UserRole;
import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Table(name = "users")
public class User extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Comment("아이디")
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Comment("비밀번호")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Comment("이름")
    @Column(name = "name", length = 50)
    private String name;

    @Comment("이메일")
    @Column(name = "email", length = 100)
    private String email;

    @Builder.Default
    @Comment("권한")
    @Column(name = "role", length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Builder.Default
    @Comment("가입일시")
    @Column(name = "signup_at", updatable = false)
    private LocalDateTime signupAt = LocalDateTime.now();

    @Comment("로그인일시")
    @Column(name = "signin_at")
    private LocalDateTime signindAt;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.signupAt = now;
    }

    /**
     * 회원정보 수정
     * @param request
     */
    public void modify(UserDto.ModifyRequest request) {
        this.setName(request.getName());
        this.setEmail(request.getEmail());
    }

    /**
     * 로그인정보 저장
     */
    public void signinModify() {
        this.setSignindAt(LocalDateTime.now());
    }

}
