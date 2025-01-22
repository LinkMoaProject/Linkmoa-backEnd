package com.linkmoa.source.domain.directory.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.*;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;
    private final SiteRepository siteRepository;

    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> createDirectory(DirectoryCreateReques requestDto, PrincipalDetails principalDetails){

        Directory parentDirectory = requestDto.parentDirectoryId() == null
                ? null
                : directoryRepository.findById(requestDto.parentDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Integer nextOrderIndex = parentDirectory.getNextOrderIndex();


        Directory newDirectory = Directory.builder()
                .directoryName(requestDto.directoryName())
                .directoryDescription(requestDto.directoryDescription())
                .orderIndex(nextOrderIndex)
                .build();

        // 부모 디렉토리에 새 디렉토리 추가
        if (parentDirectory != null) {
            parentDirectory.addChildDirectory(newDirectory);
        }

        directoryRepository.save(newDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 생성에 성공했습니다.")
                .data(newDirectory.getId())
                .build();
    }


    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> updateDirectory(DirectoryUpdateRequest requestDto, PrincipalDetails principalDetails){

        Directory updateDirectory = directoryRepository.findById(requestDto.directoryId())
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        updateDirectory.updateDirectoryNameAndDescription(requestDto.directoryName(),requestDto.directoryDescription());


        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 수정(이름,설명)에 성공했습니다.")
                .data(updateDirectory.getId())
                .build();

    }
    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> deleteDirectory(DirectoryIdRequest requestDto,
                                                          PrincipalDetails principalDetails){

        Directory deleteDirectory = directoryRepository.findById(requestDto.directoryId())
                .orElseThrow(()-> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Long directoryId = deleteDirectory.getId();
        Integer orderIndex = deleteDirectory.getOrderIndex();
        Directory parentDirectory = deleteDirectory.getParentDirectory();

        directoryRepository.decrementDirectoryAndSiteOrderIndexes(parentDirectory,orderIndex);
        directoryRepository.delete(deleteDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 삭제에 성공했습니다.")
                .data(directoryId)
                .build();
    }

    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> moveDirectory(DirectoryMoveRequest requestDto,
                                                        PrincipalDetails principalDetails){

        Directory sourceDirectory = directoryRepository.findById(requestDto.sourceDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Directory targetDirectory = directoryRepository.findById(requestDto.targetDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        sourceDirectory.setParentDirectory(targetDirectory);

        directoryRepository.decrementDirectoryAndSiteOrderIndexes(sourceDirectory, sourceDirectory.getOrderIndex());

        Integer newOrderIndex = targetDirectory.getNextOrderIndex();
        sourceDirectory.setOrderIndex(newOrderIndex);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory를 다른 Directory로 위치 이동에 성공했습니다.")
                .data(sourceDirectory.getId())
                .build();
    }

    @ValidationApplied
    public ApiDirectoryResponseSpec<DirectoryResponse> findDirectoryDetails(DirectoryIdRequest directoryIdRequest
                                                                                  , PrincipalDetails principalDetails){

        Directory targetDirectory = directoryRepository.findById(directoryIdRequest.directoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        List<DirectoryDetailResponse> directoryDetailResponses = directoryRepository.findDirectoryDetails(targetDirectory.getId());
        List<SiteDetailResponse> sitesDetails = siteRepository.findSitesDetails(targetDirectory.getId());

        DirectoryResponse directoryResponse = DirectoryResponse.builder()
                .targetDirectoryDescription(targetDirectory.getDirectoryDescription())
                .targetDirectoryName(targetDirectory.getDirectoryName())
                .directoryDetailResponses(directoryDetailResponses)
                .siteDetailResponses(sitesDetails)
                .build();

        return ApiDirectoryResponseSpec.<DirectoryResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 클릭 시, 해당 디렉토리 내에 사이트 및 디렉토리를 조회했습니다.")
                .data(directoryResponse)
                .build();

    }

    public void cloneDirectory(Long newRootDirectoryId, Long originalDirectoryId){

        Directory originalDirectory = directoryRepository.findById(originalDirectoryId)
                .orElseThrow(() -> new RuntimeException("Original directory not found"));

        Directory newParentDirectory = directoryRepository.findById(newRootDirectoryId).orElse(null);

        Directory clonedDirectory = originalDirectory.cloneDirectory(newParentDirectory);

        directoryRepository.save(clonedDirectory);
    }
}
