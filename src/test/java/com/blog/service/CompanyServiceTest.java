package com.blog.service;

import com.blog.dto.AddCompanyDto;
import com.blog.dto.CompanyListDto;
import com.blog.entity.Address;
import com.blog.entity.Company;
import com.blog.entity.Member;
import com.blog.entity.MemberStatus;
import com.blog.repository.CompanyRepository;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@EnabledIfEnvironmentVariable(
        named = "SPRING_PROFILES_ACTIVE",
        matches = "local"
)
@Transactional
class CompanyServiceTest {

    @Autowired
    CompanyService companyService;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    //@Test
    public void save() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);
        memberRepository.save(member);

        AddCompanyDto dto = AddCompanyDto.builder()
                .name("세차장1")
                .contact("01000000000")
                .lat("lat")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();

        // when
        long id = companyService.save("member1", null, dto);

        em.flush();
        em.clear();
        Company findCompany = companyService.findById(id);

        // then
        assertThat(findCompany.getName()).isEqualTo("세차장1");
        System.out.println("list.get(0).getImgPath() = " + findCompany.getImgPath());
    }

    //@Test
    public void delete() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);
        memberRepository.save(member);

        AddCompanyDto dto = AddCompanyDto.builder()
                .name("세차장1")
                .contact("01000000000")
                .lat("lat")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();

        // when
        companyService.save("member1",null,dto);

        em.flush();
        em.clear();
        List<Company> before = companyService.findAll();
        companyService.delete(before.get(0).getId());

        // then
        em.flush();
        em.clear();
        List<Company> list = companyService.findAll();
        assertThat(list.size()).isEqualTo(0);
    }

    //@Test
    public void findAllForMap() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);
        memberRepository.save(member);

        AddCompanyDto dto = AddCompanyDto.builder()
                .name("세차장1")
                .contact("01000000000")
                .lat("lat")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();
        AddCompanyDto dto2 = AddCompanyDto.builder()
                .name("세차장2")
                .contact("01000000000")
                .lat("lat2")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();
        companyService.save("member1",null,dto);
        companyService.save("member1",null,dto2);

        em.flush();
        em.clear();

        // when
        List<CompanyListDto> dtoList = companyService.findAllForMap();

        // then
        Assertions.assertThat(dtoList.size()).isEqualTo(2);
        assertThat(dtoList.get(1).getLat()).isEqualTo("lat2");
    }

    //@Test
    public void findByName() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);
        memberRepository.save(member);

        AddCompanyDto dto = AddCompanyDto.builder()
                .name("세차장1")
                .contact("01000000000")
                .lat("lat")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();
        AddCompanyDto dto2 = AddCompanyDto.builder()
                .name("세차장2")
                .contact("01000000000")
                .lat("lat2")
                .lon("lon")
                .content("content")
                .fullAddress("add")
                .city("city")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(23, 30))
                .build();
        companyService.save("member1",null,dto);
        companyService.save("member1",null,dto2);

        em.flush();
        em.clear();

        // when
        List<CompanyListDto> findList1 = companyService.findByName("세차장2");
        List<CompanyListDto> findList2 = companyService.findByName("세차장3");


        // then
        assertThat(findList1.size()).isEqualTo(1);
        assertThat(findList1.get(0).getLat()).isEqualTo("lat2");

        assertThat(findList2.size()).isEqualTo(0);
    }


}