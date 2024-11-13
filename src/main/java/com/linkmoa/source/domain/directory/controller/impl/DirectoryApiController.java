package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryUpdateResponseDto;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.domain.notify.dto.request.DirectorySendRequestDto;
import com.linkmoa.source.domain.notify.entity.DirectorySendRequest;
import com.linkmoa.source.domain.notify.service.DirectorySendRequestService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryApiController implements DirectoryApiSpecification {

    private final DirectoryService directoryService;
    private final DirectorySendRequestService directorySendRequestService;

    @PostMapping
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
    }



}
