package com.blog.repository;

import com.blog.entity.Referer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefererRepository extends JpaRepository<Referer, Long> {

    List<Referer> findByCreatedDateBetweenOrderByIdDesc(LocalDateTime beforeTime, LocalDateTime now);
}
