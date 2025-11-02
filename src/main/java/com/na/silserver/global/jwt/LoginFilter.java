package com.na.silserver.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.na.silserver.domain.token.dto.TokenDto;
import com.na.silserver.domain.token.repository.TokenRepository;
import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.global.exception.GlobalExceptionHandler;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilMessage;
import com.na.silserver.global.util.UtilProperty;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // 로그인 검증을 담당
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final UtilMessage utilMessage;

    private final Long JWT_ACCESS_EXPIRATION = Long.valueOf(UtilProperty.getProperty("spring.jwt.access.expiration"));
    private final Long JWT_REFRESH_EXPIRATION = Long.valueOf(UtilProperty.getProperty("spring.jwt.refresh.expiration"));

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            TokenRepository tokenRepository,
            ObjectMapper objectMapper,
            UtilMessage utilMessage) {

        String LOGIN_URL = UtilProperty.getProperty("custom.url.login");
        super.setFilterProcessesUrl(LOGIN_URL); // 로그인 URL 설정
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
        this.utilMessage = utilMessage;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = "";
        String password = "";

        log.debug("=== 로그인 시도 시작 ===");
        log.debug("요청 URL: {}", request.getRequestURL());
        log.debug("Content-Type: {}", request.getContentType());

        try {
            UserDto.LoginRequest loginRequestDto = objectMapper.readValue(
                    StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8),
                    UserDto.LoginRequest.class
            );
            username = loginRequestDto.getUsername();
            password = loginRequestDto.getPassword();
            log.debug("파싱된 로그인정보 - username: {}, password: [PROTECTED]", username);
        } catch (JsonMappingException e) {
            log.error("JSON 매핑 에러: {}", e.getMessage(), e);
            throw new AuthenticationException("JSON 매핑 실패") {};
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 에러: {}", e.getMessage(), e);
            throw new AuthenticationException("JSON 처리 실패") {};
        } catch (IOException e) {
            log.error("IO 에러: {}", e.getMessage(), e);
            throw new AuthenticationException("IO 처리 실패") {};
        }

        if (username == null || username.trim().isEmpty()) {
            log.error("사용자명이 비어있습니다");
            throw new AuthenticationException("사용자명이 필요합니다") {};
        }

        if (password == null || password.trim().isEmpty()) {
            log.error("비밀번호가 비어있습니다");
            throw new AuthenticationException("비밀번호가 필요합니다") {};
        }

        log.debug("인증 토큰 생성: username = {}", username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        log.debug("AuthenticationManager로 인증 시도");
        try {
            Authentication result = authenticationManager.authenticate(authToken);
            log.debug("인증 성공: {}", result.getName());
            return result;
        } catch (AuthenticationException e) {
            log.error("AuthenticationManager 인증 실패: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String memberRole = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("accessToken", username, memberRole, JWT_ACCESS_EXPIRATION * 1000L);
        String refreshToken = jwtUtil.createJwt("refreshToken", username, memberRole, JWT_REFRESH_EXPIRATION * 1000L);

        tokenRepository.deleteByUsername(username);

        TokenDto.CreateRequest createRequestDto = new TokenDto.CreateRequest();
        createRequestDto.setUsername(username);
        createRequestDto.setRefreshToken(refreshToken);
        createRequestDto.setRefreshTokenExpiration(LocalDateTime.now().plusSeconds(JWT_REFRESH_EXPIRATION));
        tokenRepository.save(createRequestDto.toEntity());

        response.setHeader("accessToken", accessToken);
        response.addCookie(UtilCommon.createCookie("refreshToken", refreshToken));

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }

    /**
     * 로그인 실패시 실행하는 메서드
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.error("=== 로그인 실패 ===");
        log.error("실패 원인: {}", failed.getClass().getSimpleName());
        log.error("실패 메시지: {}", failed.getMessage());
        log.error("스택트레이스:", failed);

        // 요청 정보 로그
        log.error("요청 URL: {}", request.getRequestURL());
        log.error("요청 Method: {}", request.getMethod());
        log.error("Content-Type: {}", request.getContentType());

        GlobalExceptionHandler.filterExceptionHandler(
                response,
                HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
                ResponseCode.LOGIN_FAIL,
                utilMessage.getMessage("login.fail", null)
        );
    }
}
