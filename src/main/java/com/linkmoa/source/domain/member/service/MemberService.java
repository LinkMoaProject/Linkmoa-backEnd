package com.linkmoa.source.domain.member.service;


import com.linkmoa.source.auth.jwt.refresh.service.RefreshTokenService;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import com.linkmoa.source.domain.memberPageLink.service.MemberPageLinkService;
import com.linkmoa.source.domain.notify.service.NotificationService;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.entity.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {


    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final NotificationService notifyService;
    private final MemberPageLinkService memberPageLinkService;


    public Member saveOrUpdate(Member member){
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();
            existingMember.updateMember(member);

            return memberRepository.save(existingMember);
        } else {
            return memberRepository.save(member);
        }
    }

    public Member findMemberByEmail(String email)  {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

        return member;
    }
    public boolean isMemberExist(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String getRedirectUrlForMember(String email){
        Member member = findMemberByEmail(email);

        if (member.getGender() == null || member.getJob() == null || member.getAge() == null) {
            return "http://localhost:3000/signup";
        }

        return "http://localhost:3000/mainpage";
    }

    public void memberSignUp(MemberSignUpRequest memberSignUpRequest, PrincipalDetails principalDetails){
        log.info("memberSignUp - email : {}", principalDetails.getEmail());
        Member member = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다."));

        member.updateSignUpMember(memberSignUpRequest.age(), memberSignUpRequest.gender(), memberSignUpRequest.job());
        memberRepository.save(member);

    }

    public void memberLogout(PrincipalDetails principalDetails){
        log.info("memberLogout - email : {}", principalDetails.getEmail());
        Member member = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다."));
        refreshTokenService.deleteRefreshToken(member.getEmail());
    }


    public ApiPageResponseSpec<List<PageResponse>> processMemberDeletion(PrincipalDetails principalDetails){
        log.info("memberDelete - email : {}", principalDetails.getEmail());
        Member member = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다."));

        List<Page> pagesWithUniqueHost = memberPageLinkService.PagesWithUniqueHostByMember(member.getId());

        List<PageResponse> pageResponses = new ArrayList<>();

        for(Page pageWithUniqueHost : pagesWithUniqueHost){
            pageResponses.add(PageResponse.builder()
                    .pageId(pageWithUniqueHost.getId())
                    .pageTitle(pageWithUniqueHost.getPageTitle())
                    .pageType(pageWithUniqueHost.getPageType())
                    .build());

        }
        return ApiPageResponseSpec.<List<PageResponse>>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("해당 회원이 유일한 호스트인 페이지 반환 완료!")
                .data(pageResponses)
                .build();
    }

    @Transactional
    public void memberDelete(PrincipalDetails principalDetails){
        log.info("memberDelete - email : {}", principalDetails.getEmail());
        Member member = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다."));

        String memberEmail = member.getEmail();

        memberPageLinkService.deleteMemberPageLink(member.getId());

        notifyService.deleteAllNotificationByMemberEmail(memberEmail);

        memberRepository.delete(member);
        refreshTokenService.deleteRefreshToken(memberEmail);
        SecurityContextHolder.clearContext();
    }

}
