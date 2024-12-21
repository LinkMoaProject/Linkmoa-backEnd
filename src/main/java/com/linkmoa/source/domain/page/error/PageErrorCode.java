package com.linkmoa.source.domain.page.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PageErrorCode implements ErrorCode {


    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"페이지를 찾을 수 없습니다."),
    CANNOT_INVITE_TO_PERSONAL_PAGE(HttpStatus.FORBIDDEN, "개인 페이지에는 초대를 보낼 수 없습니다."),
    CANNOT_LEAVE_PERSONAL_PAGE(HttpStatus.FORBIDDEN, "개인 페이지에서는 탈퇴할 수 없습니다."),
    ALREADY_JOINED_SHARED_PAGE(HttpStatus.BAD_REQUEST, "이미 참여한 공유 페이지입니다."),
    CANNOT_LEAVE_SHARED_PAGE_SINGLE_MEMBER(HttpStatus.BAD_REQUEST, "공유 페이지에 유일한 멤버일 때는, 공유 페이지를 탈퇴할 수 없습니다. 공유 페이지 삭제를 진행해주세요."),
    CANNOT_LEAVE_SHARED_PAGE_SINGLE_HOST(HttpStatus.BAD_REQUEST, "공유 페이지에 유일한 호스트 멤버일 때는, 공유 페이지를 탈퇴할 수 없습니다. 공유 페이지 삭제를 진행해주세요.");



    private HttpStatus httpStatus;
    private String errorMessage;

    PageErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
