package com.na.silserver.domain.entertain.controller;

import com.na.silserver.domain.entertain.dto.WorkDto;
import com.na.silserver.domain.entertain.service.WorkService;
import com.na.silserver.global.response.MessageResponse;
import com.na.silserver.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/work")
@RestController
@Tag(name = "엔터", description = "엔터")
public class WorkController {

    private final WorkService workService;
    private final UtilMessage utilMessage;

    @Operation(summary = "작품목록", description = "작품목록")
    @GetMapping
    public ResponseEntity<List<WorkDto.Response>> workList() {
        return ResponseEntity.ok(workService.workList());
    }

    @Operation(summary = "작품상세", description = "작품상세")
    @GetMapping("/{id}")
    public ResponseEntity<WorkDto.Response> workDetail(@PathVariable String id) {
        return ResponseEntity.ok(workService.workDetail(id));
    }

    @Operation(summary = "작품등록", description = "작품등록")
    @PostMapping
    public ResponseEntity<WorkDto.Response> workCreate(@Valid @ParameterObject WorkDto.CreateRequest request) {
        return ResponseEntity.ok(workService.workCreate(request));
    }

    @Operation(summary = "작품수정", description = "작품수정")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> workModify(@PathVariable String id, @Valid @ParameterObject WorkDto.ModifyRequest request) {
        workService.workModify(id, request);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.modify")));
    }

    @Operation(summary = "작품삭제", description = "작품삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> workDelete(@PathVariable String id) {
        workService.workDelete(id);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.delete")));
    }

}
