package com.na.silserver.domain.board.repository;

import com.na.silserver.domain.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    /**
     * 페이징
     * @param search
     * @param pageable
     * @return
     */
    Page<BoardDto.Response> searchBoards(BoardDto.Search search, Pageable pageable);

    /**
     * downloadExcel 같은 전체데이타
     * @param search
     * @return
     */
    List<BoardDto.Response> searchBoards(BoardDto.Search search);

}
