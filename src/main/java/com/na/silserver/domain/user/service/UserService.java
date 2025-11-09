package com.na.silserver.domain.user.service;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import com.na.silserver.global.exception.CustomException;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UtilMessage utilMessage;
    private final UserRepository userRepository;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<UserDto.Response> userList(UserDto.Search search) {
        // Search 정보로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                search.getPage(),
                search.getSize(),
                search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                search.getSortBy()
        );
        return userRepository.searchUsers(search, pageable);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public UserDto.Response userDetail(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data")));
        return UserDto.Response.toDto(user);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    @Transactional
    public void userModify(String id, UserDto.ModifyRequest request) {
        // S: 유효성검증
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data")));
        // E: 유효성검증

        user.modify(request);
    }

    /**
     * 삭제
     * @param id
     */
    @Transactional
    public void userDelete(String id) {
        // S: 유효성검증
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data")));
        // E: 유효성검증
        userRepository.delete(user);
    }


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

}
