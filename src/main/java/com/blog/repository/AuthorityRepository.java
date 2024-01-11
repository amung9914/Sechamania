package com.blog.repository;

import com.blog.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authorities,Long> {
}
