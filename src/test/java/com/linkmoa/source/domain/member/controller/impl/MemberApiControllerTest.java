/*
package com.linkmoa.source.domain.member.controller.impl;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.constant.Gender;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.domain.member.entity.WithMockPrincipalDetails;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.service.PageService;

@ExtendWith(MockitoExtension.class)
class MemberApiControllerTest {

	private MockMvc mockMvc;

	@Mock
	private MemberService memberService;

	@Mock
	private PageService pageService;

	@Mock
	private PrincipalDetails principalDetails;

	@InjectMocks
	private MemberApiController memberApiController;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(memberApiController).build();
	}

	@Test
	@WithMockPrincipalDetails(email = "test@example.com")
	void memberSignUp_shouldReturnSuccessResponse() throws Exception {
		// given
		MemberSignUpRequest request = new MemberSignUpRequest("20대", Gender.MALE, "student", "nickname");
		String requestBody = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/member/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.successMessage").value("회원가입 성공"))
			.andExpect(jsonPath("$.status").value("SUCCESS"))
			.andExpect(jsonPath("$.httpStatusCode").value("OK"));

		verify(memberService).memberSignUp(any(), any());
	}

}*/
