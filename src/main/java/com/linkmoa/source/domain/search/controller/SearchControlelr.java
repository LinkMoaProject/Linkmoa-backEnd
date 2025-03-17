/*
package com.linkmoa.source.domain.search.controller;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchControlelr implements SearchApiSpecification {
    private final SearchService searchService;

    @Override
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryResponse>> searchDirectoryAndSite(
            String title,
            PrincipalDetails principalDetails) {

        ApiDirectoryResponseSpec<DirectoryResponse> directoryResponse =
                searchService.searchByTitle(title, principalDetails);

        return ResponseEntity.ok().body(directoryResponse);
    }

}
*/
