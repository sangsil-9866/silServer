package com.na.silserver.domain.entertain.entity;

import com.na.silserver.domain.order.dto.TeamDto;
import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

/**
 * 아이돌팀
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Comment("팀명")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("데뷔일")
    @Column(name = "debut_day")
    private LocalDate debutDay;

    public void modify(TeamDto.ModifyRequest request){
        this.name = request.getName();
        this.debutDay = request.getDebutDay();
    }

}



