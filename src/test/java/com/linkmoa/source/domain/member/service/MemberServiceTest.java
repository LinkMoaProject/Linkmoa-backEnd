package com.linkmoa.source.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.constant.Gender;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.repository.MemberRepository;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private MemberService memberService;
	@Mock
	private PrincipalDetails principalDetails;
	private Member member;

	@BeforeEach
	void init() {

		member = Member.builder()
			.email("test")
			.role(Role.ROLE_USER)
			.provider("google")
			.providerId("google12345")
			.build();

		when(principalDetails.getEmail()).thenReturn("test");
	}

	@Test
	@DisplayName("자체 회원가입 시 회원 정보를 저장한다.")
	void successMemberSignUp_savesMember() {

		// given
		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest
			("20-30", Gender.MALE, "student", "park");

		when(memberRepository.findByEmail(principalDetails.getEmail())).thenReturn(Optional.of(member));

		// when
		memberService.memberSignUp(memberSignUpRequest, principalDetails);

		// then
		assertThat(member.getNickname()).isEqualTo("park");
		assertThat(member.getAgeRange()).isEqualTo("20-30");
		assertThat(member.getGender()).isEqualTo(Gender.MALE);
		assertThat(member.getJob()).isEqualTo("student");

		// verify
		verify(memberRepository).save(member);
	}

}