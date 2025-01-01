package com.linkmoa.source.domain.member.controller.impl;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.controller.spec.MemberApiSpecification;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberApiController implements MemberApiSpecification{

    private final MemberService memberService;
    @PostMapping("/sign-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> memberSignUp(
            MemberSignUpRequest memberSignUpRequest,
            PrincipalDetails principalDetails
    ) {
        log.info("여긴 들어오나 : {}", principalDetails.getEmail());

        memberService.memberSignUp(memberSignUpRequest,principalDetails);

        log.info("여긴 들어오나2");
        ApiResponseSpec apiResponseSpec =new ApiResponseSpec(HttpStatus.OK,"회원가입 성공");
        return ResponseEntity.ok()
                .body(apiResponseSpec);
    }


    @PostMapping("/log-out")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> memberLogout(PrincipalDetails principalDetails) {

        memberService.memberLogout(principalDetails);
        ApiResponseSpec apiResponseSpec =new ApiResponseSpec(HttpStatus.OK,"로그아웃 성공");
        return ResponseEntity.ok()
                .body(apiResponseSpec);
    }
}
