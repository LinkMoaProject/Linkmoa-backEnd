package com.linkmoa.source.domain.directory.controller.impl;


import  com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.*;
import com.linkmoa.source.domain.directory.dto.response.*;
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
            DirectoryCreateRequest directoryCreateRequest,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<Long> createDirectoryResponse = directoryService.createDirectory(directoryCreateRequest, principalDetails);
        return ResponseEntity.ok().body(createDirectoryResponse);
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

    @Override
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryPasteResponse>> pasteDirectory(
            DirectoryPasteRequest directoryPasteRequest,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectoryPasteResponse> directoryPasteResponse = directoryService.pasteDirectory(directoryPasteRequest, principalDetails);

        return ResponseEntity.ok().body(directoryPasteResponse);
    }

    @Override
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryDragAndDropResponse>> dragAndDropDirectoryOrSite
            (DirectoryDragAndDropRequest directoryDragAndDropRequest,
             PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectoryDragAndDropResponse> directoryDragAndDropResponseApiDirectoryResponse =
                directoryService.dragAndDropDirectoryOrSite(directoryDragAndDropRequest, principalDetails);

        return ResponseEntity.ok().body(directoryDragAndDropResponseApiDirectoryResponse);
    }

}
