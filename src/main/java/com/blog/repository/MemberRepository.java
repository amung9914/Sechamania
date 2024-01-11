package com.blog.repository;

import com.blog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(@Param("email")String email);
    Optional<Member> findByNickname(@Param("nickname")String nickname);
}
