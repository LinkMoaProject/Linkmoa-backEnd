package com.linkmoa.source.domain.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.jwt.refresh.service.RefreshTokenService;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import com.linkmoa.source.domain.memberPageLink.service.MemberPageLinkService;
import com.linkmoa.source.domain.notification.service.NotificationService;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;
	private final NotificationService notifyService;
	private final MemberPageLinkService memberPageLinkService;

	public Member saveOrUpdate(Member member) {
		Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

		if (optionalMember.isPresent()) {
			Member existingMember = optionalMember.get();
			existingMember.updateMember(member);

			return memberRepository.save(existingMember);
		} else {
			return memberRepository.save(member);
		}
	}

	public Member findMemberByEmail(String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

		return member;
	}

	public boolean isMemberExist(String email) {
		return memberRepository.existsByEmail(email);
	}

	public String getRedirectUrlForMember(String email) {
		Member member = findMemberByEmail(email);

		if (member.getGender() == null || member.getJob() == null || member.getAgeRange() == null) {
			return "http://localhost:3000/signup";
		}

		return "http://localhost:3000/mainpage";
	}

	public void memberSignUp(MemberSignUpRequest memberSignUpRequest, PrincipalDetails principalDetails) {

		Member member = memberRepository.findByEmail(principalDetails.getEmail())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

		member.updateSignUpMember(memberSignUpRequest.ageRange(), memberSignUpRequest.gender(),
			memberSignUpRequest.job(), memberSignUpRequest.nickName());
		memberRepository.save(member);

	}

	public void memberLogout(PrincipalDetails principalDetails) {
		Member member = memberRepository.findByEmail(principalDetails.getEmail())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));
		refreshTokenService.deleteRefreshToken(member.getEmail());
	}

	public ApiResponseSpec<List<PageResponse>> processMemberDeletion(PrincipalDetails principalDetails) {
		Member member = memberRepository.findByEmail(principalDetails.getEmail())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

		List<Page> pagesWithUniqueHost = memberPageLinkService.PagesWithUniqueHostByMember(member.getId());

		List<PageResponse> pageResponses = new ArrayList<>();

		for (Page pageWithUniqueHost : pagesWithUniqueHost) {
			pageResponses.add(PageResponse.builder()
				.pageId(pageWithUniqueHost.getId())
				.pageTitle(pageWithUniqueHost.getPageTitle())
				.pageType(pageWithUniqueHost.getPageType())
				.build());

		}
		return ApiResponseSpec.success(
			HttpStatus.OK,
			"해당 회원이 유일한 호스트인 페이지 반환 완료!",
			pageResponses
		);
	}

	@Transactional
	public void memberDelete(PrincipalDetails principalDetails) {
		Member member = memberRepository.findByEmail(principalDetails.getEmail())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

		String memberEmail = member.getEmail();

		memberPageLinkService.deleteMemberPageLink(member.getId());

		notifyService.deleteAllNotificationByMember(findMemberByEmail(memberEmail));

		memberRepository.delete(member);
		refreshTokenService.deleteRefreshToken(memberEmail);
		SecurityContextHolder.clearContext();
	}

}
