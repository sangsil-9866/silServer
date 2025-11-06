package com.na.silserver.domain.board.entity;

import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Table(name = "board")
public class Board extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Comment("제목")
    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Comment("내용")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Comment("조회수")
    @Column(name = "views", nullable = false)
    @Builder.Default
    private Long views = 0L;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardFile> boardFiles = new ArrayList<>();

    // == 연관관계 편의 메서드
    public void addBoardFile(BoardFile boardFile) {
        this.boardFiles.add(boardFile);
        boardFile.setBoard(this);
    }
}
