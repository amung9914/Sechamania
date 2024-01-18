package com.blog.config.oauth;



import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.entity.MemberStatus;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    /**
     * 리소스 서버에서 보내주는 사용자 정보를 불러온다
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // 사용자 객체를 불러옴
        saveOrUpdate(user);
        return user;
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성
    @Transactional
    public Member saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.updateNickname(name))
                .orElseGet(()->{
                    Member newMember = Member.builder()
                            .email(email)
                            .nickname(name)
                            .status(MemberStatus.ACTIVE)
                            .build();
                    memberRepository.save(newMember);

                    Authorities auth = new Authorities("ROLE_USER");
                    auth.makeRole(newMember);
                    authorityRepository.save(auth);
                    return newMember;
                });
        return member;
    }



}
