package com.na.silserver.domain.user.service;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import com.na.silserver.global.exception.CustomException;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UtilMessage utilMessage;
    private final UserRepository userRepository;

    /**
     * 사용자가입
     * @param request
     * @return
     */
    public UserDto.Response signup (UserDto.SignupRequest request) {

        // S: 유효성검증
        // E: 유효성검증

        // 엔티티로 변환하기 전에 비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userRepository.save(request.toEntity());
        return UserDto.Response.toDto(user);

    }



    /**
     * 목록
     * @param search
     * @return
     */
    public Page<UserDto.Response> userList(UserDto.Search search) {
        List<User> users = userRepository.findAll();

        // 📦 페이징 + 정렬
        Sort.Direction direction = search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, search.getSortBy()));

        // DTO 변환
        List<UserDto.Response> content = users.stream()
                .map(UserDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, 10);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public UserDto.Response userDetail(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        return UserDto.Response.toDto(user);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    @Transactional
    public UserDto.Response userModify(String id, UserDto.ModifyRequest request) {
        // S: 유효성검증
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        // E: 유효성검증

        user.modify(request);
        return UserDto.Response.toDto(user);
    }

    /**
     * 삭제
     * @param id
     */
    @Transactional
    public void userDelete(String id) {
        // S: 유효성검증
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        // E: 유효성검증
        userRepository.deleteById(user.getId());
    }

}
