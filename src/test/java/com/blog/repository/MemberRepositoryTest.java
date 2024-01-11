package com.blog.repository;

import com.blog.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void save() throws Exception {
        // given
        Member m1 = Member.builder().email("member1").password("1234")
                .nickname("membernick1").build();

        // when
        memberRepository.save(m1);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findByEmail(m1.getEmail());
        // then
        Assertions.assertThat(findMember.get().getEmail()).isEqualTo(m1.getEmail());
    }


}