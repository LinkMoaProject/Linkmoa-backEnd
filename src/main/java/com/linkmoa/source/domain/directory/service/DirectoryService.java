package com.linkmoa.source.domain.directory.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDeleteRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectoryService {

    private final PageRepository pageRepository;
    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;


    @Transactional
    @ValidationApplied
    public ApiDirectoryResponseSpec<Long> createDirectory(DirectoryCreateRequestDto directoryCreateRequestDto,PrincipalDetails principalDetails){

        Page page =pageRepository.findById(directoryCreateRequestDto.baseRequestDto().pageId())
                .orElseThrow(()-> new PageException(PageErrorCode.PAGE_NOT_FOUND));


        Directory parentDirectory = directoryCreateRequestDto.parentDirectoryId() == null
                ? null
                : directoryRepository.findById(directoryCreateRequestDto.parentDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        Directory newDirectory = Directory.builder()
                .directoryName(directoryCreateRequestDto.directoryName())
                .directoryDescription(directoryCreateRequestDto.directoryDescription())
                .page(page)
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
    public ApiDirectoryResponseSpec<Long> updateDirectory(DirectoryUpdateRequestDto directoryUpdateRequestDto,PrincipalDetails principalDetails){

        Directory updateDirectory = directoryRepository.findById(directoryUpdateRequestDto.directoryId())
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        updateDirectory.updateDirectoryNameAndDescription(directoryUpdateRequestDto.direcotryName(),directoryUpdateRequestDto.directoryDescription());


        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 수정에 성공했습니다.")
                .data(updateDirectory.getId())
                .build();

    }






    @Transactional
    public ApiDirectoryResponseSpec<Long> deleteDirectory(DirectoryDeleteRequestDto directoryDeleteRequestDto, PrincipalDetails principalDetails){

        Directory deleteDirectory = directoryRepository.findById(directoryDeleteRequestDto.directoryId())
                .orElseThrow(()-> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        directoryRepository.delete(deleteDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 삭제에 성공 했습니다.")
                .data(deleteDirectory.getId())
                .build();
    }


    /*


    *//** 삭제, 조회 그리고 수정은 direcotry 데이터의
     *  memberId와 principal의 정보를 이용해서 데이터에 관한 올바른 접근 권한이 있는지 확인 해야됨
     *  1.direcotry의 member와 principalDetails로 찾은 member가 같은 객체인지 확인 해야됨
     *  2.일치 => 정상 응답
     *    불일치 => 에러 응답
     *  AOP로 추후 구현 예정
     **//*

    @Transactional
    public ApiDirectoryResponseSpec<Long> deleteDirectory(Long directoryId, PrincipalDetails principalDetails){
        Directory deleteDirectory =directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Member member =memberService.findMemberByEmail(principalDetails.getEmail());

        // AOP 구현 부분

        directoryRepository.delete(deleteDirectory);

        return ApiDirectoryResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 삭제에 성공하였습니다.")
                .data(deleteDirectory.getId())
                .build();

    }


    @Transactional
    public ApiDirectoryResponseSpec<DirectoryUpdateResponseDto> updateDirectory(DirectoryUpdateRequestDto directoryUpdateRequestDto, PrincipalDetails principalDetails){
        Directory updateDirectory = directoryRepository.findById(directoryUpdateRequestDto.directoryId())
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        // AOP 구현 부분

        updateDirectory.updateDirectoryName(directoryUpdateRequestDto.direcotryName());

        DirectoryUpdateResponseDto directoryUpdateResponseDto = DirectoryUpdateResponseDto.builder()
                .directoryName(updateDirectory.getDirectoryName())
                .parentDirectoryId(updateDirectory.getParentDirectory().getId())
                .build();


        return ApiDirectoryResponseSpec.<DirectoryUpdateResponseDto>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("directory 수정에 성공했습니다.")
                .data(directoryUpdateResponseDto)
                .build();
    }


*/




}
