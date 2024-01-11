package com.blog.service;

import com.blog.dto.AddUserRequest;
import com.blog.entity.Member;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void join() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("member1", "nick1", "pass", "add", "lat", "lon");

        // when
        memberService.join(request1);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findByNickname("nick1");

        // then
        Assertions.assertThat(findMember.get().getEmail()).isEqualTo(request1.getEmail());
    }

    @Test
    public void duplicatedEmail() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("member1", "nick1", "pass", "add", "lat", "lon");

        // when
        memberService.join(request1);
        em.flush();
        em.clear();
        AddUserRequest request2 = new AddUserRequest("member1", "nick2", "pass", "add", "lat", "lon");


        //then
        assertThrows(IllegalStateException.class, () ->{
            memberService.join(request2);
        });


    }

    @Test
    public void duplicatedNickname() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("member1", "nick1", "pass", "add", "lat", "lon");

        // when
        memberService.join(request1);
        em.flush();
        em.clear();
        AddUserRequest request2 = new AddUserRequest("member2", "nick1", "pass", "add", "lat", "lon");


        //then
        assertThrows(IllegalStateException.class, () ->{
            memberService.join(request2);
        });

    }

}