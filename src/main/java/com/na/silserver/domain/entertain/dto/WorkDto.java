package com.na.silserver.domain.entertain.dto;

import com.na.silserver.domain.entertain.entity.Work;
import com.na.silserver.domain.entertain.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class WorkDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String description;
        private Genre genre;
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static WorkDto.Response toDto(Work work){
            return WorkDto.Response.builder()
                    .id(work.getId())
                    .title(work.getTitle())
                    .description(work.getDescription())
                    .genre(work.getGenre())
                    .createdBy(work.getCreatedBy())
                    .createdAt(work.getCreatedAt())
                    .modifiedBy(work.getModifiedBy())
                    .modifiedAt(work.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        @Schema(description = "제목")
        @NotBlank
        @Size(max = 250)
        private String title;

        @Schema(description = "설명")
        private String description;

        private Genre genre;

        public Work toEntity() {
            return Work.builder()
                    .title(title)
                    .description(description)
                    .genre(genre)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        @Schema(description = "제목")
        @NotBlank
        @Size(max = 250)
        private String title;

        @Schema(description = "설명")
        private String description;

        private Genre genre;
    }

}
