package com.na.silserver.global.jwt;

import com.na.silserver.global.exception.GlobalExceptionHandler;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final UtilMessage utilMessage;
	/**
	 * 인증실패
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
		log.debug("::::::::::::::::::::::::::::::JwtAuthenticationEntryPoint(인증실패)::::::::::::::::::::::::::::");
		log.debug(request.getRequestURL().toString());
		GlobalExceptionHandler.filterExceptionHandler(response, HttpStatus.UNAUTHORIZED, ResponseCode.AUTHENTICATION_DENIED, utilMessage.getMessage("authentication.denied", null));
	}
}