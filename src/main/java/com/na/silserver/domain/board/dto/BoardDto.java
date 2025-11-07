package com.na.silserver.domain.board.dto;

import com.na.silserver.domain.board.entity.Board;
import com.na.silserver.domain.board.entity.BoardFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {

    /**
     * 조회조건
     */
    @Getter
    @Setter
    public static class Search {

        @Schema(description = "제목, 내용")
        private String keyword;

        @Schema(description = "등록일 시작 (yyyy-MM-dd)", example = "2025-01-01")
        private LocalDate fromDate;

        @Schema(description = "등록일 종료 (yyyy-MM-dd)", example = "2030-12-31")
        private LocalDate toDate;

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        private int page = 0;

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        private int size = 10;

        @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
        private String sortBy = "createdAt";

        @Schema(description = "내림차순 여부", example = "true", defaultValue = "true")
        private boolean desc = true;
    }

    /**
     * 조회
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String content;
        private Long views;
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;
        private List<BoardFile> files;

        public static BoardDto.Response toDto(Board board) {
            return Response.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .views(board.getViews())
                    .createdBy(board.getCreatedBy())
                    .createdAt(board.getCreatedAt())
                    .modifiedBy(board.getModifiedBy())
                    .modifiedAt(board.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        @Schema(description = "제목", example = "제목")
        @NotBlank
        @Size(max = 250)
        private String title;

        @Schema(description = "내용", example = "내용")
        @NotBlank
        private String content;

        private List<MultipartFile> boardFiles;

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    /**
     * 수정
     */
    @Getter
    @Setter
    public static class ModifyRequest {
        @Schema(description = "제목")
        @NotBlank
        @Size(max = 250)
        private String title;

        @Schema(description = "내용")
        @NotBlank
        private String content;

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }

}
