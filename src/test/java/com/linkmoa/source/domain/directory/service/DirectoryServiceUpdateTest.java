package com.linkmoa.source.domain.directory.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class DirectoryServiceUpdateTest {

    /*@Mock
    private MemberService memberService;

    @Mock
    private DirectoryRepository directoryRepository;

    @InjectMocks
    private DirectoryService directoryService;

    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Member 객체 생성
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .nickname("TestUser")
                .provider("google")
                .providerId("google123")
                .build();

        // Member의 id 필드 값 강제 설정
        Field idField = member.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        // PrincipalDetails 생성
        principalDetails = new PrincipalDetails(member);


        // lenient를 사용하여 memberService의 동작 설정을 무시 가능하도록 설정
        lenient().when(memberService.findMemberByEmail("test@example.com")).thenReturn(member);
    }

    @Test
    @DisplayName("directory name 및 description 수정")
    void testDirectoryUpdate_Success() throws NoSuchFieldException, IllegalAccessException {
        // given
        DirectoryUpdateRequestDto requestDto = new DirectoryUpdateRequestDto(
                new BaseRequestDto(1L, CommandType.EDIT),
                "디렉토리 이름 수정 후",
                "디렉토리 설명 수정 후",
                1L
        );

        Page page = Page.builder()
                .pageTitle("Sample Page")
                .pageDescription("Sample Description")
                .pageType(PageType.PERSONAL)
                .build();

        Directory directory = createDirectory(1L, page);


        // Mock 설정
        when(directoryRepository.findById(1L)).thenReturn(Optional.of(directory));

        // when
        ApiDirectoryResponseSpec<Long> response = directoryService.updateDirectory(requestDto, principalDetails);

        // then
        assertNotNull(response);
        assertEquals(1L,response.getData());
        verify(directoryRepository, times(1)).findById(1L);
    }

    private Directory createDirectory(Long id,Page page){

        Directory directory = Directory.builder()
                .directoryName("디렉토리 이름 수정 전")
                .directoryDescription("디렉토리 설명 수정 전")
                .parentDirectory(null)
                .build();

        ReflectionTestUtils.setField(directory,"id",id);

        return directory;
    }
*/

}


