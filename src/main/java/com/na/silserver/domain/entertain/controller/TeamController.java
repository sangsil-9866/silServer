package com.na.silserver.domain.entertain.controller;

import com.na.silserver.domain.entertain.service.TeamService;
import com.na.silserver.domain.order.dto.TeamDto;
import com.na.silserver.global.response.ApiResponse;
import com.na.silserver.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/team")
@RestController
@Tag(name = "엔터", description = "엔터")
public class TeamController {

    private final TeamService teamService;
    private final UtilMessage utilMessage;

    @Operation(summary = "팀목록", description = "팀목록")
    @GetMapping
    public ResponseEntity<List<TeamDto.Response>> teamList() {
        return ResponseEntity.ok(teamService.teamList());
    }

    @Operation(summary = "팀상세", description = "팀상세")
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto.Response> teamDetail(@PathVariable String id) {
        return ResponseEntity.ok(teamService.teamDetail(id));
    }

    @Operation(summary = "팀등록", description = "팀등록")
    @PostMapping
    public ResponseEntity<TeamDto.Response> teamCreate(@Valid @ParameterObject TeamDto.CreateRequest request) {
        return ResponseEntity.ok(teamService.teamCreate(request));
    }

    @Operation(summary = "팀수정", description = "팀수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> teamModify(@PathVariable String id, @Valid @ParameterObject TeamDto.ModifyRequest request) {
        teamService.teamModify(id, request);
        return ResponseEntity.ok(ApiResponse.of(utilMessage.getMessage("success.modify")));
    }

    @Operation(summary = "팀삭제", description = "팀삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> teamDelete(@PathVariable String id) {
        teamService.teamDelete(id);
        return ResponseEntity.ok(ApiResponse.of(utilMessage.getMessage("success.delete")));
    }

}
