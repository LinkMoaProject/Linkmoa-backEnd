package com.linkmoa.source.domain.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.service.PageService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

	private final MemberService memberService;
	private final PageService pageService;

	@PostMapping("/sign-up")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberSignUp(
		@RequestBody @Validated MemberSignUpRequest memberSignUpRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		memberService.memberSignUp(memberSignUpRequest, principalDetails);
		pageService.createPersonalPage(principalDetails);

		return ResponseEntity.ok()
			.body(ApiResponseSpec.success(HttpStatus.OK, "회원 가입 성공 "));
	}

	@PostMapping("/log-out")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberLogout(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		memberService.memberLogout(principalDetails);
		return ResponseEntity.ok()
			.body(ApiResponseSpec.success(HttpStatus.OK, "로그아웃 성공 "));
	}

	@GetMapping("/deletion-process")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<List<PageResponse>>> memberDeletionProcess(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<List<PageResponse>> pagesWithUniqueHost = memberService.processMemberDeletion(
			principalDetails);
		return ResponseEntity.ok()
			.body(pagesWithUniqueHost);
	}

	@DeleteMapping("/deletion")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec> memberDeletion(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		memberService.memberDelete(principalDetails);
		return ResponseEntity.ok()
			.body(ApiResponseSpec.success(HttpStatus.OK, "회원 탈퇴 성공 "));
	}
}
