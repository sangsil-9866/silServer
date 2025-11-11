package com.na.silserver.domain.order.dto;

import com.na.silserver.domain.entertain.entity.Team;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TeamDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String name;
        private LocalDate debutDay;
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static TeamDto.Response toDto(Team team){
            return Response.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .debutDay(team.getDebutDay())
                    .createdAt(team.getCreatedAt())
                    .createdBy(team.getCreatedBy())
                    .modifiedAt(team.getModifiedAt())
                    .modifiedBy(team.getModifiedBy())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        private String name;
        private LocalDate debutDay;

        public Team toEntity() {
            return Team.builder()
                    .name(name)
                    .debutDay(debutDay)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        private String name;
        private LocalDate debutDay;
    }

}
