package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.*;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.service.DirectoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
@Slf4j
public class DirectoryApiController implements DirectoryApiSpecification {

    private final DirectoryService directoryService;

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> createDirectory(
            DirectoryCreateReques directoryCreateReques,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<Long> createDirectroyResponse = directoryService.createDirectory(directoryCreateReques, principalDetails);
        return ResponseEntity.ok().body(createDirectroyResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> updateDirectory(
            DirectoryUpdateRequest directoryUpdateRequest,
            PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponseSpec<Long> updateDirectoryResponse = directoryService.updateDirectory(directoryUpdateRequest, principalDetails);
        return ResponseEntity.ok().body(updateDirectoryResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirectory(
            DirectoryIdRequest directoryIdRequestDto,
            PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponseSpec<Long> deleteDirectoryResponse = directoryService.deleteDirectory(directoryIdRequestDto, principalDetails);
        return ResponseEntity.ok().body(deleteDirectoryResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> moveDirectory(
        DirectoryMoveRequest directoryMoveRequest,
        PrincipalDetails principalDetails
    ){
        ApiDirectoryResponseSpec<Long> moveDirectoryResponse = directoryService.moveDirectory(directoryMoveRequest, principalDetails);
        return ResponseEntity.ok().body(moveDirectoryResponse);
    }


    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryResponse>> getDirectory
            (DirectoryIdRequest directoryIdRequest,
             PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectoryResponse> directoryResponse = directoryService.findDirectoryDetails(directoryIdRequest, principalDetails);
        return ResponseEntity.ok().body(directoryResponse);
    }

}
