package com.na.silserver.domain.entertain.service;

import com.na.silserver.domain.entertain.dto.ArtistDto;
import com.na.silserver.domain.entertain.dto.WorkDto;
import com.na.silserver.domain.entertain.entity.Artist;
import com.na.silserver.domain.entertain.repository.ArtistRepository;
import com.na.silserver.domain.entertain.repository.TeamRepository;
import com.na.silserver.domain.entertain.repository.WorkRepository;
import com.na.silserver.global.util.UtilCommon;
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
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final WorkRepository workRepository;
    private final TeamRepository teamRepository;
    private final UtilMessage utilMessage;

    public List<ArtistDto.Response> artistList() {
        return artistRepository.findAll().stream()
                .map(ArtistDto.Response::toDto)
                .toList();
    }

    public ArtistDto.Response artistDetail(Long id){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        return ArtistDto.Response.toDto(artist);
    }

    @Transactional
    public ArtistDto.Response artistCreate(ArtistDto.CreateRequest request) {
        Artist artist = artistRepository.save(request.toEntity());
        for(String item : request.getWorkIds()){
            artist.getWorks().add(workRepository.findById(item).orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data"))));
        }

        if(UtilCommon.isNotEmpty(request.getTeamId())){
            artist.setTeam(teamRepository.findById(request.getTeamId()).orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data"))));
        }
        return ArtistDto.Response.toDto(artist);
    }

    @Transactional
    public void artistModify(Long id, ArtistDto.ModifyRequest request) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        artist.modify(request);
        for(WorkDto.CreateRequest item : request.getWorks()){
            artist.getWorks().add(item.toEntity());
        }
    }

    @Transactional
    public void artistDelete(Long id) {
        artistRepository.deleteById(id);
    }

}
