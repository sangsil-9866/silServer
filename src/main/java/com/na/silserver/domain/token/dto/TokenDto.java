package com.na.silserver.domain.token.dto;

import com.na.silserver.domain.token.entity.Token;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class TokenDto {

	@Getter
	@Setter
	public static class CreateRequest {
		private String username;
		private String refreshToken;
		private LocalDateTime refreshTokenExpiration;

		// DTO -> Entity 로 변환
		public Token toEntity() {
			return Token.builder()
					.username(username)
					.refreshToken(refreshToken)
					.refreshTokenExpiration(refreshTokenExpiration)
					.build();
		}
	}

}
