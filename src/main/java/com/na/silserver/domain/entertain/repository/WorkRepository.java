package com.na.silserver.domain.entertain.repository;

import com.na.silserver.domain.entertain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {
}
