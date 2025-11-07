package com.na.silserver.domain.board.dto;

import com.na.silserver.domain.board.entity.BoardFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BoardFileDto {

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long id;
        private String uploadPath;      // 업로드경로
        private String originalFileName;        // 사용자가 업로드한 파일명
        private String storedFileName;          // 서버에 저장될 실제 파일명
        private Long fileSize;       // 파일 크기

        public static Response toDto(BoardFile boardFile) {
            return Response.builder()
                    .id(boardFile.getId())
                    .uploadPath(boardFile.getUploadPath())
                    .originalFileName(boardFile.getOriginalFileName())
                    .storedFileName(boardFile.getStoredFileName())
                    .fileSize(boardFile.getFileSize())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {

        @Schema(description = "업로드경로")
        @NotBlank
        @Size(max = 20)
        private String uploadPath;

        @Schema(description = "사용자가 업로드한 파일명")
        @NotBlank
        private String originalFileName;

        @Schema(description = "서버에 저장될 실제 파일명")
        @NotBlank
        private String storedFileName;

        @Schema(description = "파일 크기")
        @Positive
        private Long fileSize;

        public BoardFile toEntity() {
            return BoardFile.builder()
                    .uploadPath(uploadPath)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .fileSize(fileSize)
                    .build();
        }
    }
}
