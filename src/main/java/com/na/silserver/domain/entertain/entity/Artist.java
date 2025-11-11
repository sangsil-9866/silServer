package com.na.silserver.domain.entertain.entity;

import com.na.silserver.domain.entertain.dto.ArtistDto;
import com.na.silserver.domain.entertain.enums.Gender;
import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 배우 및 가수
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist")
public class Artist extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("성별")
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.WOMEN;

    @Comment("출생년월")
    @Column(name = "birthday")
    private LocalDate birthday;

    @Comment("데뷔일")
    @Column(name = "debut_day")
    private LocalDate debutDay;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "artist_id")
    @Builder.Default
    private List<Work> works = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void modify(ArtistDto.ModifyRequest request) {
        gender = request.getGender();
        birthday = request.getBirthday();
        debutDay = request.getDebutDay();
    }

}
