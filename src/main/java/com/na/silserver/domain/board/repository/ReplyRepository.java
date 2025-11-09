package com.na.silserver.domain.board.repository;

import com.na.silserver.domain.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, String> {
    List<Reply> findByBoardIdOrderByCreatedAtAsc(String boardId);
}
