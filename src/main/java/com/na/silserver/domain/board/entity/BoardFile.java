package com.na.silserver.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_file")
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("업로드경로")
    @Column(name="UPLOAD_PATH", nullable = false, length = 20)
    private String uploadPath;

    @Comment("사용자가 업로드한 파일명")
    @Column(name="ORIGINAL_FILE_NAME", nullable = false)
    private String originalFileName;

    @Comment("서버에 저장될 실제 파일명")
    @Column(name="STORED_FILE_NAME", nullable = false, length = 50)
    private String storedFileName;

    @Comment("파일 크기")
    @Column(name="FILE_SIZE", nullable = false)
    private Long fileSize;       // 파일 크기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}
