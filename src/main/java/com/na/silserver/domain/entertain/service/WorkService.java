package com.na.silserver.domain.entertain.service;

import com.na.silserver.domain.entertain.dto.WorkDto;
import com.na.silserver.domain.entertain.entity.Work;
import com.na.silserver.domain.entertain.repository.WorkRepository;
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
public class WorkService {

    private final WorkRepository workRepository;
    private final UtilMessage utilMessage;

    public List<WorkDto.Response> workList() {
        return workRepository.findAll().stream()
                .map(WorkDto.Response::toDto)
                .toList();
    }

    public WorkDto.Response workDetail(String id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        return WorkDto.Response.toDto(work);
    }

    @Transactional
    public WorkDto.Response workCreate(WorkDto.CreateRequest request) {
        Work work = workRepository.save(request.toEntity());
        return WorkDto.Response.toDto(work);
    }

    @Transactional
    public void workModify(String id, WorkDto.ModifyRequest request) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(utilMessage.getMessage("notfound.data")));
        work.modify(request);
    }

    @Transactional
    public void workDelete(String id) {
        workRepository.deleteById(id);
    }
}
