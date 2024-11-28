package com.linkmoa.source.domain.directory.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDeleteRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryMoveRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DirectoryService {

    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;


    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> createDirectory(DirectoryCreateRequestDto requestDto,PrincipalDetails principalDetails){

        Directory parentDirectory = requestDto.parentDirectoryId() == null
                ? null
                : directoryRepository.findById(requestDto.parentDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        Directory newDirectory = Directory.builder()
                .directoryName(requestDto.directoryName())
                .directoryDescription(requestDto.directoryDescription())
                .parentDirectory(parentDirectory)
                .build();

        directoryRepository.save(newDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 생성에 성공했습니다.")
                .data(newDirectory.getId())
                .build();
    }

    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> updateDirectory(DirectoryUpdateRequestDto requestDto,PrincipalDetails principalDetails){

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
    public ApiDirectoryResponseSpec<Long> deleteDirectory(DirectoryDeleteRequestDto requestDto,
                                                          PrincipalDetails principalDetails){

        Directory deleteDirectory = directoryRepository.findById(requestDto.directoryId())
                .orElseThrow(()-> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        directoryRepository.delete(deleteDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 삭제에 성공했습니다.")
                .data(deleteDirectory.getId())
                .build();
    }

    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> moveDirectory(DirectoryMoveRequestDto requestDto,
                                                        PrincipalDetails principalDetails){

        Directory sourceDirectory = directoryRepository.findById(requestDto.sourceDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Directory targetDirectory = directoryRepository.findById(requestDto.targetDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        sourceDirectory.setParentDirectory(targetDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 위치 이동에 성공했습니다.")
                .data(sourceDirectory.getId())
                .build();
    }

}
