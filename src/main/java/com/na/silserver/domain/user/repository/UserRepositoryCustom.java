package com.na.silserver.domain.user.repository;

import com.na.silserver.domain.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    /**
     * 페이징
     * @param search
     * @param pageable
     * @return
     */
    Page<UserDto.Response> searchUsers(UserDto.Search search, Pageable pageable);

    /**
     * downloadExcel 같은 전체데이타
     * @param search
     * @return
     */
    List<UserDto.Response> searchUsers(UserDto.Search search);
}
