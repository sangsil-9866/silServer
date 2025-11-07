package com.na.silserver.domain.board.controller;

import com.na.silserver.domain.board.dto.BoardDto;
import com.na.silserver.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시판", description = "게시판 API")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판목록", description = "게시판목록")
    @GetMapping
    public ResponseEntity<Page<BoardDto.Response>> boardList(@ParameterObject @ModelAttribute BoardDto.Search search) {
        Page<BoardDto.Response> boards = boardService.boardList(search);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @Operation(summary = "게시판등록", description = "게시판등록")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BoardDto.Response> boardCreate(@Valid BoardDto.CreateRequest request) throws IOException {
        BoardDto.Response result = boardService.boardCreate(request);
        return ResponseEntity.ok(result);
    }

}
