package com.blog.repository;

import com.blog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("select m from Member m join fetch m.authorities a where m.email = :email")
    Optional<Member> findByEmail(@Param("email")String email);
    Optional<Member> findByNickname(@Param("nickname")String nickname);
}
