package com.linkmoa.source.domain.member.service;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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


}
