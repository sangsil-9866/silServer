package com.na.silserver.domain.entertain.dto;

import com.na.silserver.domain.entertain.entity.Artist;
import com.na.silserver.domain.entertain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ArtistDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private Gender gender;
        private LocalDate birthday;
        private LocalDate debutDay;
        private String createdBy;
        private LocalDate createdAt;
        private String modifiedBy;
        private LocalDate modifiedAt;
        private List<WorkDto.Response> works;

        public static ArtistDto.Response toDto(Artist artist) {
            return Response.builder()
                    .id(artist.getId())
                    .name(artist.getName())
                    .gender(artist.getGender())
                    .birthday(artist.getBirthday())
                    .birthday(artist.getBirthday())
                    .debutDay(artist.getDebutDay())
                    .works(artist.getWorks().stream()
                            .map(WorkDto.Response::toDto)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        @Schema(description = "이름")
        @NotBlank
        @Size(max = 200)
        private String name;

        @Schema(description = "성별")
        private Gender gender;

        @Schema(description = "생일")
        private LocalDate birthday;

        @Schema(description = "데뷔일")
        private LocalDate debutDay;

        @Schema(description = "작품")
        private List<String> workIds;

        @Schema(description = "팀아이디")
        private String teamId;

        public Artist toEntity() {
            return Artist.builder()
                    .name(name)
                    .gender(gender)
                    .birthday(birthday)
                    .debutDay(debutDay)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        @Schema(description = "성별")
        private Gender gender;

        @Schema(description = "생일")
        private LocalDate birthday;

        @Schema(description = "데뷔일")
        private LocalDate debutDay;

        @Schema(description = "작품")
        private List<WorkDto.CreateRequest> works;
    }

}
