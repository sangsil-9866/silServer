package com.na.silserver.domain.board.controller;

import com.na.silserver.domain.board.dto.ReplyDto;
import com.na.silserver.domain.board.service.ReplyService;
import com.na.silserver.global.response.MessageResponse;
import com.na.silserver.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardId}/reply")
@Tag(name = "게시판", description = "게시판 API 댓글")
public class ReplyController {

    private final ReplyService replyService;
    private final UtilMessage utilMessage;

    @Operation(summary = "댓글목록", description = "댓글목록")
    @GetMapping
    public ResponseEntity<List<ReplyDto.Response>> replyList(@PathVariable String boardId) {
        List<ReplyDto.Response> results = replyService.replyList(boardId);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "댓글등록", description = "댓글등록")
    @PostMapping
    public ResponseEntity<ReplyDto.Response> replyCreate(
            @PathVariable String boardId,
            @AuthenticationPrincipal(expression = "username") String username,
            @ParameterObject @Valid ReplyDto.CreateRequest request
    ) {
        log.debug("username: {}", username);
        ReplyDto.Response result = replyService.replyCreate(boardId, username, request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "댓글수정", description = "댓글수정")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> replyModify(
            @PathVariable String boardId,
            @PathVariable String id,
            @AuthenticationPrincipal(expression = "username") String username,
            @ParameterObject @Valid ReplyDto.ModifyRequest request
    ) {
        replyService.replyModify(id, username, request);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.modify")));
    }

    @Operation(summary = "댓글삭제", description = "댓글삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> replyDelete(
            @PathVariable String boardId,
            @PathVariable String id,
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        replyService.replyDelete(id, username);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.delete")));
    }
}
