package com.linkmoa.source.domain.directory.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryUpdateResponseDto;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final MemberService memberService;


    @Transactional
    public ApiDirectoryResponse<Long> saveDirectory(DirectoryCreateRequestDto directoryCreateRequestDto, PrincipalDetails principalDetails){
        Member member = memberService.findMemberByEmail(principalDetails.getEmail());

        Directory newDirectory =Directory.builder()
                .directoryName(directoryCreateRequestDto.directoryName())
                .member(member)
                .build();

        if(directoryCreateRequestDto.parentDirectoryId()!=null){
            Directory parentDirectory = directoryRepository.findById(directoryCreateRequestDto.parentDirectoryId())
                    .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

            parentDirectory.addChildDirectory(newDirectory);

            directoryRepository.save(newDirectory);
            directoryRepository.save(parentDirectory);
        }
        else{
            directoryRepository.save(newDirectory);
        }

        return ApiDirectoryResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 생성에 성공했습니다.")
                .data(newDirectory.getId())
                .build();

    }

    /** 삭제, 조회 그리고 수정은 direcotry 데이터의
     *  memberId와 principal의 정보를 이용해서 데이터에 관한 올바른 접근 권한이 있는지 확인 해야됨
     *  1.direcotry의 member와 principalDetails로 찾은 member가 같은 객체인지 확인 해야됨
     *  2.일치 => 정상 응답
     *    불일치 => 에러 응답
     *  AOP로 추후 구현 예정
     **/

    @Transactional
    public ApiDirectoryResponse<Long> deleteDirectory(Long directoryId,PrincipalDetails principalDetails){
        Directory deleteDirectory =directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Member member =memberService.findMemberByEmail(principalDetails.getEmail());

        // AOP 구현 부분

        directoryRepository.delete(deleteDirectory);

        return ApiDirectoryResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 삭제에 성공하였습니다.")
                .data(deleteDirectory.getId())
                .build();

    }


    @Transactional
    public ApiDirectoryResponse<DirectoryUpdateResponseDto> updateDirectory(DirectoryUpdateRequestDto directoryUpdateRequestDto, PrincipalDetails principalDetails){
        Directory updateDirectory = directoryRepository.findById(directoryUpdateRequestDto.directoryId())
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        // AOP 구현 부분

        updateDirectory.updateDirectoryName(directoryUpdateRequestDto.direcotryName());

        DirectoryUpdateResponseDto directoryUpdateResponseDto = DirectoryUpdateResponseDto.builder()
                .directoryName(updateDirectory.getDirectoryName())
                .parentDirectoryId(updateDirectory.getParentDirectory().getId())
                .build();


        return ApiDirectoryResponse.<DirectoryUpdateResponseDto>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("directory 수정에 성공했습니다.")
                .data(directoryUpdateResponseDto)
                .build();
    }






}
