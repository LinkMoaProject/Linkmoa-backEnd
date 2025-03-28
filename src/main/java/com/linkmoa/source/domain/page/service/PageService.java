package com.linkmoa.source.domain.page.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.Favorite.service.FavoriteService;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import com.linkmoa.source.global.dto.request.BaseRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PageService {

	private final PageRepository pageRepository;
	private final MemberService memberService;
	private final DirectoryRepository directoryRepository;
	private final MemberPageLinkRepository memberPageLinkRepository;
	private final SiteRepository siteRepository;
	private final PageAsyncService pageAsyncService;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	@Transactional
	public ApiPageResponseSpec<Long> createPersonalPage(PrincipalDetails principalDetails) {

		Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

		// 개인 페이지가 이미 존재하면 예외 발생 (반환값을 사용하지 않고 존재 여부만 확인)
		if (memberPageLinkRepository.findPersonalPageByMemberId(hostMember.getId()).isPresent()) {
			throw new MemberException(MemberErrorCode.MEMBER_EXIST_EMAIL);
		}

		PageCreateRequest requestDto = PageCreateRequest.builder()
			.pageTitle(hostMember.getEmail() + " 개인")
			.pageDescription(hostMember.getEmail() + " 의 개인 페이지 입니다.")
			.pageType(PageType.PERSONAL)
			.build();

		Directory rootDirectory = createRootDirectory(hostMember);
		Page newPage = createNewPage(requestDto, rootDirectory);
		MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
		saveEntities(newPage, memberPageLink, rootDirectory);

		return ApiPageResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage(newPage.getPageType().toString() + "페이지 생성에 성공했습니다.")
			.data(newPage.getId())
			.build();
	}

	@Transactional
	public ApiPageResponseSpec<Long> createPage(PageCreateRequest requestDto, PrincipalDetails principalDetails) {

		Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

		Directory rootDirectory = createRootDirectory(hostMember);
		Page newPage = createNewPage(requestDto, rootDirectory);
		MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
		saveEntities(newPage, memberPageLink, rootDirectory);

		return ApiPageResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage(newPage.getPageType().toString() + "페이지 생성에 성공했습니다.")
			.data(newPage.getId())
			.build();
	}

	private Page createNewPage(PageCreateRequest requestDto, Directory rootDirectory) {
		return Page.builder()
			.pageType(requestDto.pageType())
			.pageTitle(requestDto.pageTitle())
			.pageDescription(requestDto.pageDescription())
			.rootDirectory(rootDirectory)
			.build();
	}

	private Directory createRootDirectory(Member hostMember) {
		return Directory.builder()
			.directoryName(hostMember.getEmail() + "님의 Root directory name")
			.directoryDescription(hostMember.getEmail() + "님의 Root directory description")
			.build();
	}

	private MemberPageLink createMemberPageLink(Member hostMember, Page page) {
		return MemberPageLink.builder()
			.member(hostMember)
			.page(page)
			.permissionType(PermissionType.HOST)
			.build();
	}

	@Transactional
	public void saveEntities(Page page, MemberPageLink memberPageLink, Directory rootDirectory) {
		pageRepository.save(page);
		memberPageLinkRepository.save(memberPageLink);
		directoryRepository.save(rootDirectory);
	}

	@Transactional
	@ValidationApplied
	public ApiPageResponseSpec<Long> deletePage(PageDeleteRequest pageDeleteRequest,
		PrincipalDetails principalDetails) {
		pageRepository.deleteById(pageDeleteRequest.baseRequest().pageId());
		return ApiPageResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("페이지 삭제에 성공했습니다.")
			.data(pageDeleteRequest.baseRequest().pageId())
			.build();
	}

	public ApiPageResponseSpec<List<PageResponse>> findAllPages(PrincipalDetails principalDetails) {
		List<PageResponse> allPagesByMemberId = pageRepository.findAllPagesByMemberId(principalDetails.getId());

		return ApiPageResponseSpec.<List<PageResponse>>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("현재 회원이 참여 중인 모든 페이지를 조회했습니다.")
			.data(allPagesByMemberId)
			.build();

	}

	@Transactional
	public ApiPageResponseSpec<SharePageLeaveResponse> leaveSharePage(BaseRequest baseRequest,
		PrincipalDetails principalDetails) {

		Page page = pageRepository.findById(baseRequest.pageId()).
			orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		Member member = memberService.findMemberByEmail(principalDetails.getEmail());

		validateCanLeaveSharePage(page, member);

		memberPageLinkRepository.deleteByMemberIdAndPageId(member.getId(), page.getId());

		SharePageLeaveResponse sharePageLeaveResponse = SharePageLeaveResponse.builder()
			.pageId(page.getId())
			.pageTitle(page.getPageTitle())
			.build();

		return ApiPageResponseSpec.<SharePageLeaveResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("공유 페이지 탈퇴에 성공했습니다.")
			.data(sharePageLeaveResponse)
			.build();
	}

	private void validateCanLeaveSharePage(Page page, Member member) {

		if (page.getPageType() == PageType.PERSONAL) {
			throw new PageException(PageErrorCode.CANNOT_LEAVE_PERSONAL_PAGE);
		}

		if (memberPageLinkRepository.countMembersInSharedPage(page.getId()) == 1) {
			throw new PageException(PageErrorCode.CANNOT_LEAVE_SHARED_PAGE_SINGLE_MEMBER);
		}

		if (memberPageLinkRepository.countHostMembersInSharedPage(page.getId(), member) == 1) {
			throw new PageException(PageErrorCode.CANNOT_LEAVE_SHARED_PAGE_SINGLE_HOST);
		}

	}

	/**
	 * findPageMain 비동기 방식
	 * @param baseRequest
	 * @param principalDetails
	 * @return
	 */
	public ApiPageResponseSpec<PageDetailsResponse> getPageMain(BaseRequest baseRequest,
		PrincipalDetails principalDetails) {
		Page page = pageRepository.findById(baseRequest.pageId())
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		PageDetailsResponse pageDetailsResponse = getPageDetailsResponse(page, principalDetails);

		return ApiPageResponseSpec.<PageDetailsResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("페이지 접속 시, 해당 페이지 메인화면을 조회합니다")
			.data(pageDetailsResponse)
			.build();
	}

	private PageDetailsResponse getPageDetailsResponse(Page page, PrincipalDetails principalDetails) {
		Long directoryId = page.getRootDirectory().getId();

		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		Set<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(favorites);
		Set<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(favorites);

		CompletableFuture<PageDetailsResponse> pageDetailsResponseCompletableFuture =
			pageAsyncService.combinePageDetails(
				pageAsyncService.findDirectoryDetailsAsync(directoryId, favoriteDirectoryIds)

				, pageAsyncService.findSitesDetailsAsync(directoryId, favoriteSiteIds), page);

		// 비동기 작업이 완료되면 결과를 가져와 ApiPageResponseSpec으로 포장하여 반환
		PageDetailsResponse pageDetailsResponse = pageDetailsResponseCompletableFuture.join();
		return pageDetailsResponse;
	}

	public ApiPageResponseSpec<PageDetailsResponse> loadPersonalPageMain(PrincipalDetails principalDetails) {
		Member member = memberService.findMemberByEmail(principalDetails.getEmail());

		Page personalPage = memberPageLinkRepository.findPersonalPageByMemberId(member.getId())
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		PageDetailsResponse pageDetailsResponse = getPageDetailsResponse(personalPage, principalDetails);

		return ApiPageResponseSpec.<PageDetailsResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("로그인 성공 시, 유저의 개인 페이지 메인 화면 데이터를 조회합니다.")
			.data(pageDetailsResponse)
			.build();
	}

	public Page getPersonalPage(Long memberId) {
		return memberPageLinkRepository.findPersonalPageByMemberId(memberId)
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));
	}

}
