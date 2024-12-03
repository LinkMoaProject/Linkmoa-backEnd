package com.linkmoa.source.domain.member.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.linkmoa.source.domain.member.error.MemberErrorCode.MEMBER_NOT_FOUND_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {


    private final MemberRepository memberRepository;

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

        if (member.getSex() == null || member.getJob() == null || member.getAge() == null) {
            return "http://localhost:3000/signup";
        }

        return "http://localhost:3000/mainpage";
    }

    public void memberSignUp(MemberSignUpRequest memberSignUpRequest, PrincipalDetails principalDetails){
        log.info("email 입니당 : ", principalDetails.getEmail());
        Member member = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        member.updateSignUpMember(memberSignUpRequest.age(), memberSignUpRequest.sex(), memberSignUpRequest.job());

        memberRepository.save(member);
    }


}
