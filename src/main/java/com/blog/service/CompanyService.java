package com.blog.service;

import com.blog.dto.AddCompanyDto;
import com.blog.dto.AddUserRequest;
import com.blog.dto.CompanyListDto;
import com.blog.entity.Company;
import com.blog.entity.Member;
import com.blog.repository.CompanyRepository;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;

    public long save(String email, String imgPath, AddCompanyDto dto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지않는 email입니다"));
        Company company = Company.builder()
                .name(dto.getName())
                .member(member)
                .address(dto.getAddress())
                .contact(dto.getContact())
                .content(dto.getContent())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .imgPath(imgPath==null?"/img/companyDefault.jpg":imgPath)
                .build();
        return companyRepository.save(company).getId();
    }

    public List<CompanyListDto> findAllForMap(){
        List<Company> all = companyRepository.findAllForMap();
        return all.stream().map(company -> new CompanyListDto(company.getName(),company.getAddress())).toList();
    }

    public Company findById(long id){
        return companyRepository.findById(id)
                .orElseThrow((()-> new IllegalArgumentException("not found company :"+id)));
    }

    public List<CompanyListDto> findByName(String name){
        List<Company> list = companyRepository.findAllByName(name);
        return list.stream().map(company -> new CompanyListDto(company.getName(),company.getAddress())).toList();
    }

    public List<Company> findAll(){
        return companyRepository.findAll();
    }

    public void delete(long id){
        Company company = companyRepository.findById(id)
                .orElseThrow((() -> new IllegalArgumentException("not found company :" + id)));
        companyRepository.delete(company);
    }
}
