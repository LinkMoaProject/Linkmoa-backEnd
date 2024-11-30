package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.*;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectorySendResponseDto;
import com.linkmoa.source.domain.directory.error.DirectorySendRequest;
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
            DirectoryCreateRequestDto directoryCreateRequestDto,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<Long> createDirectroyResponse = directoryService.createDirectory(directoryCreateRequestDto, principalDetails);
        return ResponseEntity.ok().body(createDirectroyResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> updateDirectory(
            DirectoryUpdateRequestDto directoryUpdateRequestDto,
            PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponseSpec<Long> updateDirectoryResponse = directoryService.updateDirectory(directoryUpdateRequestDto, principalDetails);
        return ResponseEntity.ok().body(updateDirectoryResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirectory(
            DirectoryDeleteRequestDto directoryDeleteRequestDto,
            PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponseSpec<Long> deleteDirectoryResponse = directoryService.deleteDirectory(directoryDeleteRequestDto, principalDetails);
        return ResponseEntity.ok().body(deleteDirectoryResponse);
    }

    public ResponseEntity<ApiDirectoryResponseSpec<Long>> moveDirectory(
        DirectoryMoveRequestDto directoryMoveRequestDto,
        PrincipalDetails principalDetails
    ){
        ApiDirectoryResponseSpec<Long> moveDirectoryResponse = directoryService.moveDirectory(directoryMoveRequestDto, principalDetails);
        return ResponseEntity.ok().body(moveDirectoryResponse);
    }
    public ResponseEntity<ApiDirectoryResponseSpec<DirectorySendResponseDto>> sendDirectory(
            DirectorySendRequestDto directorySendRequestDto,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectorySendResponseDto> direcotrySendResponse = directoryService.mapToDirectorySendResponse(
                directoryService.createDirectorySendRequest(
                        directorySendRequestDto,
                        principalDetails)
        );
        return ResponseEntity.ok().body(direcotrySendResponse);
    }



}
