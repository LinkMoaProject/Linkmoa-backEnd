package com.linkmoa.source.domain.memberPageLink.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.repository.PageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberPageLinkService {
	private final MemberPageLinkRepository memberPageLinkRepository;
	private final PageRepository pageRepository;

	public List<Page> PagesWithUniqueHostByMember(Long memberId) {
		List<Page> uniqueHostPages = new ArrayList<>();
		List<MemberPageLink> uniqueHostLinks = memberPageLinkRepository.findUniqueHostByMemberId(memberId);
		log.info("실행은 됐음 PagesWithUniqueHostByMember");
		for (MemberPageLink uniqueHostLink : uniqueHostLinks) {
			Page page = uniqueHostLink.getPage();
			log.info("uniqueHostPages : {} ", page.getId());
			if (page != null) {
				uniqueHostPages.add(page);
			}
		}
		return uniqueHostPages;
	}

	//개인 페이지 및 유일한 host인 공유 페이지 삭제
	public void deleteMemberPageLink(Long memberId) {
		// 유일한 호스트인 페이지 목록을 조회
		List<Page> uniqueHostPages = PagesWithUniqueHostByMember(memberId);

		if (!uniqueHostPages.isEmpty()) {
			// 유일한 호스트인 페이지가 있다면 해당 페이지 삭제
			for (Page page : uniqueHostPages) {
				log.info("uniqueHostPages : {} ", page.getId());
				pageRepository.deleteById(page.getId());
			}
		}

		// MemberPageLink는 항상 삭제
		memberPageLinkRepository.deleteByMemberId(memberId);
	}

}
