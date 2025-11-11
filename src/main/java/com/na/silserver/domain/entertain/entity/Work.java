package com.na.silserver.domain.entertain.entity;

import com.na.silserver.domain.entertain.dto.WorkDto;
import com.na.silserver.domain.entertain.enums.Genre;
import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work")
public class Work extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("설명")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Comment("장르")
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    public void modify(WorkDto.ModifyRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.genre = request.getGenre();
    }
}
