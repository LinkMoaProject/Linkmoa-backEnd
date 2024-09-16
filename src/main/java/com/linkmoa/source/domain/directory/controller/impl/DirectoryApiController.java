package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.controller.spec.DirectoryApiSpecification;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryUpdateResponseDto;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<Long>> saveDirectory(
            @RequestBody @Validated DirectoryCreateRequestDto directoryCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            )
    {
        ApiDirectoryResponse<Long> apiDirectoryResponse = directoryService.saveDirectory(directoryCreateRequestDto,principalDetails);

        return ResponseEntity.ok().body(apiDirectoryResponse);
    }

    @DeleteMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<Long>> deleteDirecotry(
            @PathVariable("directoryId") @NotBlank Long directoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        ApiDirectoryResponse<Long> apiDirectoryResponse = directoryService.deleteDirectory(directoryId, principalDetails);

        return ResponseEntity.ok().body(apiDirectoryResponse);

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<DirectoryUpdateResponseDto>> updatedirecotry(
            @RequestBody DirectoryUpdateRequestDto directoryUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){
        ApiDirectoryResponse<DirectoryUpdateResponseDto> directoryUpdateResponseDtoApiDirectoryResponse = directoryService.updateDirectory(directoryUpdateRequestDto, principalDetails);


        return ResponseEntity.ok().body(directoryUpdateResponseDtoApiDirectoryResponse);

    }





}
