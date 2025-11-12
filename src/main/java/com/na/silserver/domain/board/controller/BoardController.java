package com.na.silserver.domain.board.controller;

import com.na.silserver.domain.board.dto.BoardDto;
import com.na.silserver.domain.board.service.BoardService;
import com.na.silserver.global.response.MessageResponse;
import com.na.silserver.global.util.UtilMessage;
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
    private final UtilMessage utilMessage;

    /**
     * 게시판목록
     * @param search
     * @return
     */
    @Operation(summary = "게시판목록", description = "게시판목록")
    @GetMapping
    public ResponseEntity<Page<BoardDto.Response>> boardList(@ParameterObject BoardDto.Search search) {
        Page<BoardDto.Response> results = boardService.boardList(search);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    /**
     * 게시판상세
     * @param id
     * @return
     */
    @Operation(summary = "게시판상세", description = "게시판상세")
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto.Response> boardDetail(@PathVariable String id) {
        BoardDto.Response result = boardService.boardDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 게시판등록
     * @param request
     * @return
     * @throws IOException
     */
    @Operation(summary = "게시판등록", description = "게시판등록")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BoardDto.Response> boardCreate(@Valid BoardDto.CreateRequest request) throws IOException {
        BoardDto.Response result = boardService.boardCreate(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 게시판수정
     * @param id
     * @param request
     * @return
     * @throws IOException
     */
    @Operation(summary = "게시판수정", description = "게시판수정")
    @PutMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MessageResponse> boardModify(@PathVariable String id,
                                                   @Valid BoardDto.ModifyRequest request) throws IOException {
        boardService.boardModify(id, request);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.modify")));
    }

    /**
     * 게시판삭제
     * @param id
     * @return
     */
    @Operation(summary = "게시판삭제", description = "게시판삭제")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<MessageResponse> boardDelete(@PathVariable String id) {
        boardService.boardDelete(id);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.delete")));
    }
}
