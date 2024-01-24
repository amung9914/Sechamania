package com.blog.service;

import com.blog.entity.Referer;
import com.blog.repository.RefererRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefererService {

    private final RefererRepository refererRepository;

    @Transactional
    public void save(Referer referer){
        refererRepository.save(referer);
    }

    public List<Referer> findAll(){
        return refererRepository.findByCreatedDateBetweenOrderByIdDesc(LocalDateTime.now().minusDays(3), LocalDateTime.now());
    }

}
