package com.na.silserver.domain.entertain.service;

import com.na.silserver.domain.entertain.entity.Team;
import com.na.silserver.domain.entertain.repository.TeamRepository;
import com.na.silserver.domain.order.dto.TeamDto;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtilMessage utilMessage;

    public List<TeamDto.Response> teamList() {
        return teamRepository.findAll().stream()
                .map(TeamDto.Response::toDto)
                .toList();
    }

    public TeamDto.Response teamDetail(String id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        return TeamDto.Response.toDto(team);
    }

    @Transactional
    public TeamDto.Response teamCreate(TeamDto.CreateRequest request) {
        Team team = teamRepository.save(request.toEntity());
        return TeamDto.Response.toDto(team);
    }

    @Transactional
    public void teamModify(String id, TeamDto.ModifyRequest request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        team.modify(request);
    }

    @Transactional
    public void teamDelete(String id) {
        teamRepository.deleteById(id);
    }
}
