package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDeleteRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryMoveRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.domain.notify.dto.request.DirectorySendRequestDto;
import com.linkmoa.source.domain.notify.entity.DirectorySendRequest;
import com.linkmoa.source.domain.notify.service.DirectorySendRequestService;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
@Slf4j
public class DirectoryApiController implements DirectoryApiSpecification {

    private final DirectoryService directoryService;
    private final DirectorySendRequestService directorySendRequestService;

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



  /*  @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<Long>> saveDirectory(
            @RequestBody @Validated DirectoryCreateRequestDto directoryCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            )
    {
        ApiDirectoryResponseSpec<Long> apiDirectoryResponseSpec = directoryService.saveDirectory(directoryCreateRequestDto,principalDetails);

        return ResponseEntity.ok().body(apiDirectoryResponseSpec);
    }

    @DeleteMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirecotry(
            @PathVariable("directoryId") @NotBlank Long directoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        ApiDirectoryResponseSpec<Long> apiDirectoryResponseSpec = directoryService.deleteDirectory(directoryId, principalDetails);

        return ResponseEntity.ok().body(apiDirectoryResponseSpec);

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryUpdateResponseDto>> updatedirecotry(
            @RequestBody DirectoryUpdateRequestDto directoryUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){
        ApiDirectoryResponseSpec<DirectoryUpdateResponseDto> directoryUpdateResponseDto = directoryService.updateDirectory(directoryUpdateRequestDto, principalDetails);


        return ResponseEntity.ok().body(directoryUpdateResponseDto);

    }


    @PostMapping("/send-request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<Long>> createDirectorySendRequest(
            @RequestBody DirectorySendRequestDto directorySendRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){
        DirectorySendRequest sendRequest = directorySendRequestService.createDirectorySendRequest(directorySendRequest);

        ApiDirectoryResponseSpec<Long> response =ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("directory 전송 요청 성공했습니다.")
                .data(sendRequest.getDirectorySendRequestId())
                .build();
        return ResponseEntity.ok().body(response);
    }*/


}
