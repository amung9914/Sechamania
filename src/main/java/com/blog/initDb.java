package com.blog;

import com.blog.entity.Address;
import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.entity.MemberStatus;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final MemberRepository memberRepository;
        private final AuthorityRepository authorityRepository;

        public void dbInit1(){
            Member newMember = Member.builder()
                    .email("admin@admin.com")
                    .password("$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe")
                    .nickname("admin")
                    .address(new Address("fulladdr","city","lat","lon"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("img/defaultProfile.jpg")
                    .build();
            memberRepository.save(newMember);

            Authorities userAuth = new Authorities("USER");
            authorityRepository.save(userAuth);
            userAuth.makeRole(newMember);

            Authorities companyAuth = new Authorities("COMPANY");
            authorityRepository.save(companyAuth);
            companyAuth.makeRole(newMember);

            Authorities adminAuth = new Authorities("ADMIN");
            authorityRepository.save(adminAuth);
            adminAuth.makeRole(newMember);
        }
    }
}


