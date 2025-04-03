package com.linkmoa.source.domain.page.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.favorite.entity.Favorite;
import com.linkmoa.source.domain.favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.favorite.service.FavoriteService;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.dto.request.PageCreateDto;
import com.linkmoa.source.domain.page.dto.request.PageDeleteDto;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
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
	private final PageAsyncService pageAsyncService;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	/**
	 *  회원 가입 시 자동으로 생성되는 개인 페이지 생성 로직.
	 *  해당 이메일로 이미 개인 페이지가 존재하는 경우 예외가 발생함.
	 * @param principalDetails
	 * @return
	 */
	@Transactional
	public void createPersonalPage(PrincipalDetails principalDetails) {

		Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

		validatePersonalPageNotExists(hostMember);

		PageCreateDto.Request requestDto = PageCreateDto.Request.builder()
			.pageTitle(hostMember.getEmail() + " 개인")
			.pageDescription(hostMember.getEmail() + " 의 개인 페이지 입니다.")
			.pageType(PageType.PERSONAL)
			.build();

		Directory rootDirectory = createRootDirectory(hostMember);
		Page newPage = createNewPage(requestDto, rootDirectory);
		MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
		saveEntities(newPage, memberPageLink, rootDirectory);

		return;
	}

	/**
	 * 해당 회원이 개인 페이지가 이미 존재하면 예외 발생 (반환값을 사용하지 않고 존재 여부만 확인)
	 * @param hostMember
	 */
	private void validatePersonalPageNotExists(Member hostMember) {

		if (memberPageLinkRepository.findPersonalPageByMemberId(hostMember.getId()).isPresent()) {
			throw new MemberException(MemberErrorCode.MEMBER_EXIST_EMAIL);
		}
	}

	/**
	 * 공유 페이지를 생성하는 서비스 로직.
	 * 개인 페이지는 생성할 수 없으며, 이를 사전에 검증한다.
	 * @param requestDto
	 * @param principalDetails
	 * @return
	 */
	@Transactional
	public Page createSharedPage(PageCreateDto.Request requestDto, PrincipalDetails principalDetails) {

		validatePageTypeIsNotPersonal(requestDto);
		Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

		Directory rootDirectory = createRootDirectory(hostMember);
		Page newPage = createNewPage(requestDto, rootDirectory);
		MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
		saveEntities(newPage, memberPageLink, rootDirectory);

		return newPage;
	}

	private static void validatePageTypeIsNotPersonal(PageCreateDto.Request request) {
		PageType pageType = request.pageType();
		if (pageType.equals(PageType.PERSONAL)) {
			throw new PageException(PageErrorCode.PERSONAL_PAGE_ALREADY_EXISTS);
		}
	}

	private Page createNewPage(PageCreateDto.Request request, Directory rootDirectory) {
		return Page.builder()
			.pageType(request.pageType())
			.pageTitle(request.pageTitle())
			.pageDescription(request.pageDescription())
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
	public Long deletePage(PageDeleteDto.Request request,
		PrincipalDetails principalDetails) {
		pageRepository.deleteById(request.baseRequest().pageId());
		return request.baseRequest().pageId();
	}

	public List<PageResponse> findAllPages(PrincipalDetails principalDetails) {
		List<PageResponse> allPagesByMemberId = pageRepository.findAllPagesByMemberId(principalDetails.getId());

		return allPagesByMemberId;

	}

	@Transactional
	public SharePageLeaveResponse leaveSharePage(BaseRequest baseRequest,
		PrincipalDetails principalDetails) {

		Page page = pageRepository.findById(baseRequest.pageId()).
			orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		Member member = memberService.findMemberByEmail(principalDetails.getEmail());

		validateCanLeaveSharePage(page, member);

		memberPageLinkRepository.deleteByMemberIdAndPageId(member.getId(), page.getId());

		return SharePageLeaveResponse.builder()
			.pageId(page.getId())
			.pageTitle(page.getPageTitle())
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
	public PageDetailsResponse getPageMain(BaseRequest baseRequest,
		PrincipalDetails principalDetails) {
		Page page = pageRepository.findById(baseRequest.pageId())
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		return getPageDetailsResponse(page, principalDetails);
	}

	private PageDetailsResponse getPageDetailsResponse(Page page, PrincipalDetails principalDetails) {
		Long directoryId = page.getRootDirectory().getId();

		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		List<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(favorites);
		List<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(favorites);

		CompletableFuture<PageDetailsResponse> pageDetailsResponseCompletableFuture =
			pageAsyncService.combinePageDetails(
				pageAsyncService.findDirectoryDetailsAsync(directoryId, favoriteDirectoryIds)

				, pageAsyncService.findSitesDetailsAsync(directoryId, favoriteSiteIds), page);

		// 비동기 작업이 완료되면 결과를 가져와 ApiPageResponseSpec으로 포장하여 반환
		PageDetailsResponse pageDetailsResponse = pageDetailsResponseCompletableFuture.join();
		return pageDetailsResponse;
	}

	public PageDetailsResponse loadPersonalPageMain(PrincipalDetails principalDetails) {
		Member member = memberService.findMemberByEmail(principalDetails.getEmail());

		Page personalPage = memberPageLinkRepository.findPersonalPageByMemberId(member.getId())
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		return getPageDetailsResponse(personalPage, principalDetails);
	}

	public Page getPersonalPage(Long memberId) {
		return memberPageLinkRepository.findPersonalPageByMemberId(memberId)
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));
	}

}
