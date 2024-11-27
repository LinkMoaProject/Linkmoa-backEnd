package com.linkmoa.source.domain.page.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequestDto;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.repository.PageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final MemberRepository memberRepository;
    private final DirectoryRepository directoryRepository;
    private final MemberPageLinkRepository memberPageLinkRepository;


    @Transactional
    public ApiPageResponseSpec<Long> createPage(PageCreateRequestDto requestDto, PrincipalDetails principalDetails) {

        Page newPage = Page.builder()
                .pageType(requestDto.pageType())
                .pageTitle(requestDto.pageTitle())
                .pageDescription(requestDto.pageDescription())
                .build();

        Member hostMember = memberRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

        MemberPageLink memberPageLink = MemberPageLink.builder()
                .member(hostMember)
                .page(newPage)
                .permissionType(PermissionType.HOST)
                .build();

        Directory rootDirectory = Directory.builder()
                .directoryName(hostMember.getEmail()+"님의 Root directory name")
                .directoryDescription(hostMember.getEmail()+"님의 Root directory description")
                .parentDirectory(null)
                .page(newPage)
                .build();

        pageRepository.save(newPage);
        memberPageLinkRepository.save(memberPageLink);
        directoryRepository.save(rootDirectory);


        return ApiPageResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 생성에 성공했습니다.")
                .data(newPage.getId())
                .build();
    }
}
