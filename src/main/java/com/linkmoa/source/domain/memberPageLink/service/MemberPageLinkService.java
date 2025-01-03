package com.linkmoa.source.domain.memberPageLink.service;

import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.entity.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberPageLinkService {
    private final MemberPageLinkRepository memberPageLinkRepository;

    public List<Page> PagesWithUniqueHostByMember(Long memberId) {
        List<Page> uniqueHostPages = new ArrayList<>();
        List<MemberPageLink> uniqueHostLinks = memberPageLinkRepository.findUniqueHostByMemberId(memberId);

        for (MemberPageLink uniqueHostLink : uniqueHostLinks) {
            Page page = uniqueHostLink.getPage();
            if (page != null) {
                uniqueHostPages.add(page);
            }
        }
        return uniqueHostPages;
    }

    //개인 페이지 및 유일한 host인 공유 페이지 삭제
    public void deleteMemberPageLink(Long memberId){
        memberPageLinkRepository.deleteByMemberId(memberId);
    }



}
