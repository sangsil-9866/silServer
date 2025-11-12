package com.na.silserver.domain.entertain.controller;

import com.na.silserver.domain.entertain.dto.ArtistDto;
import com.na.silserver.domain.entertain.service.ArtistService;
import com.na.silserver.global.response.MessageResponse;
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
@RequestMapping("/api/artist")
@RestController
@Tag(name = "엔터", description = "엔터")
public class ArtistController {

    private final ArtistService artistService;
    private final UtilMessage utilMessage;

    @Operation(summary = "아티스트목록", description = "아티스트목록")
    @GetMapping
    public ResponseEntity<List<ArtistDto.Response>> artistList() {
        return ResponseEntity.ok(artistService.artistList());
    }

    @Operation(summary = "아티스트상세", description = "아티스트상세")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto.Response> artistDetail(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.artistDetail(id));
    }

    @Operation(summary = "아티스트등록", description = "아티스트등록")
    @PostMapping
    public ResponseEntity<ArtistDto.Response> artistCreate(@Valid @ParameterObject ArtistDto.CreateRequest request) {
        return ResponseEntity.ok(artistService.artistCreate(request));
    }

    @Operation(summary = "아티스트수정", description = "아티스트수정")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> artistModify(@PathVariable Long id, @Valid @ParameterObject ArtistDto.ModifyRequest request) {
        artistService.artistModify(id, request);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.modify")));
    }

    @Operation(summary = "아티스트삭제", description = "아티스트삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> artistDelete(@PathVariable Long id) {
        artistService.artistDelete(id);
        return ResponseEntity.ok(MessageResponse.of(utilMessage.getMessage("success.delete")));
    }

}
