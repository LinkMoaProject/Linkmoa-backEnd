package com.linkmoa.source.auth.oauth2.service;

import com.linkmoa.source.auth.oauth2.entity.GoogleUserDetails;
import com.linkmoa.source.auth.oauth2.entity.OAuth2UserInfo;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("CustomOauth2UserService getAttributes : {}",oAuth2User.getAttributes());
       String provider = userRequest.getClientRegistration().getRegistrationId();
       OAuth2UserInfo oAuth2UserInfo = toOAuth2UserInfo(provider,oAuth2User);

        Member member = memberService.saveOrUpdate(Member.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .role(Role.ROLE_USER)
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getProviderId())
                .build()) ;


        PrincipalDetails principalDetails = new PrincipalDetails(member, oAuth2User.getAttributes());
        return principalDetails; // => DefaultOAuth2UserService의 메소드인 loadUser의 반환값은
    }

    // 서비스에 따라 OAuth2UserInfo 객체 생성 메서드
    private static OAuth2UserInfo toOAuth2UserInfo(String provider, OAuth2User oAuth2User) {

        switch (provider){
            case "google":
                return new GoogleUserDetails(oAuth2User.getAttributes());
            /*case "kakao":
                return new KakaoUserDetails(oAuth2User.getAttributes());*/
            default:
                throw new RuntimeException();
        }
    }

}
