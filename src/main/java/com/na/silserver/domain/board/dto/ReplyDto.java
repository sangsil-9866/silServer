package com.na.silserver.domain.board.dto;

import com.na.silserver.domain.board.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    @Setter
    public static class CreateRequest {
        @Schema(description = "댓글")
        @NotBlank
        private String content;
        private String parentId;   // null 이면 최상위 댓글

        public Reply toEntity() {
            return Reply.builder()
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        @Schema(description = "댓글")
        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String content;
        private String username;
        private List<Response> children;
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static ReplyDto.Response toDto(Reply reply) {
            return ReplyDto.Response.builder()
                    .id(reply.getId())
                    .content(reply.getContent())
                    .username(reply.getUser().getUsername())
                    .children(new ArrayList<>())
                    .createdBy(reply.getCreatedBy())
                    .createdAt(reply.getCreatedAt())
                    .modifiedBy(reply.getModifiedBy())
                    .modifiedAt(reply.getModifiedAt())
                    .build();
        }
    }
}
