package com.na.silserver.domain.board.repository;

import com.na.silserver.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {
}
