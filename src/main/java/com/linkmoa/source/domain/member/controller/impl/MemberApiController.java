package com.linkmoa.source.domain.member.controller.impl;

import com.google.protobuf.Api;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.controller.spec.MemberApiSpecification;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.service.PageService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberApiController implements MemberApiSpecification {

	private final MemberService memberService;
	private final PageService pageService;

	@PostMapping("/sign-up")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberSignUp(
		MemberSignUpRequest memberSignUpRequest,
		PrincipalDetails principalDetails
	) {
		memberService.memberSignUp(memberSignUpRequest, principalDetails);
		pageService.createPersonalPage(principalDetails);

		ApiResponseSpec apiResponseSpec = new ApiResponseSpec(HttpStatus.OK, "회원가입 성공");
		return ResponseEntity.ok()
			.body(apiResponseSpec);
	}

	@PostMapping("/log-out")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberLogout(PrincipalDetails principalDetails) {

		memberService.memberLogout(principalDetails);
		ApiResponseSpec apiResponseSpec = new ApiResponseSpec(HttpStatus.OK, "로그아웃 성공");
		return ResponseEntity.ok()
			.body(apiResponseSpec);
	}

	@GetMapping("/deletion-process")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<List<PageResponse>>> memberDeletionProcess(
		PrincipalDetails principalDetails) {

		ApiPageResponseSpec<List<PageResponse>> pagesWithUniqueHost = memberService.processMemberDeletion(
			principalDetails);
		return ResponseEntity.ok()
			.body(pagesWithUniqueHost);
	}

	@DeleteMapping("/deletion")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberDeletion(PrincipalDetails principalDetails) {
		memberService.memberDelete(principalDetails);
		ApiResponseSpec apiResponseSpec = new ApiResponseSpec(HttpStatus.OK, "회원 탈퇴 성공");
		return ResponseEntity.ok()
			.body(apiResponseSpec);
	}
}
