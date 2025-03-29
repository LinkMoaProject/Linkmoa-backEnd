package com.linkmoa.source.domain.search.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import com.linkmoa.source.domain.member.entity.WithMockPrincipalDetails;
import com.linkmoa.source.domain.search.constant.SearchType;
import com.linkmoa.source.domain.search.dto.request.SearchRequest;
import com.linkmoa.source.domain.search.dto.response.SearchPageResponse;
import com.linkmoa.source.domain.search.service.SearchService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

@ExtendWith(MockitoExtension.class)
class SearchApiControllerTest {

	private MockMvc mockMvc;
	@Mock
	private SearchService searchService;

	@InjectMocks
	private SearchApiController searchApiController;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(searchApiController).build();

	}

	@Test
	@WithMockPrincipalDetails(email = "test@example.com")
	void getDirectoryAndSiteByKeyword() throws Exception {
		// given
		SearchRequest searchRequest = new SearchRequest(1L, "ab", SearchType.TITLE);
		String requestBody = objectMapper.writeValueAsString(searchRequest);

		// ✅ 응답 mock
		ApiResponseSpec<SearchPageResponse> mockResponse = ApiResponseSpec.<SearchPageResponse>builder()
			.status(org.springframework.http.HttpStatus.OK)
			.message("사이트 내에 있는 모든 디렉토리와 사이트를 제목으로 검색합니다.")
			.data(SearchPageResponse.builder()
				.directorySimpleResponses(List.of()) // 빈 리스트 또는 mock 데이터
				.siteSimpleResponses(List.of())
				.build())
			.build();

		when(searchService.searchDirectoriesAndSitesByTitleInPage(any(), any())).thenReturn(mockResponse);

		// when + then
		mockMvc.perform(post("/api/search")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.successMessage").value("사이트 내에 있는 모든 디렉토리와 사이트를 제목으로 검색합니다."))
			.andExpect(jsonPath("$.status").value("SUCCESS"))
			.andExpect(jsonPath("$.httpStatusCode").value("OK"));

		verify(searchService).searchDirectoriesAndSitesByTitleInPage(any(), any());
	}

}