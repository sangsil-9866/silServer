package com.na.silserver.domain.token.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * 리프레시토큰관리
 * 필드 추가등 학인필요
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "token")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="id", nullable = false, length = 36)
	private String id;

//	@Comment("아이디")
	@Column(name = "username", unique = true, nullable = false, length = 50)
	private String username;

//	@Comment("리프레시토큰")
	@Column(name = "refresh_token", nullable = false, length = 300)
	private String refreshToken;

//	@Comment("리프레시토큰만료일시")
	@Column(name = "refresh_token_expiration")
	private LocalDateTime refreshTokenExpiration;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
    }
}
