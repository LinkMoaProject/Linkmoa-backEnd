package com.linkmoa.source.domain.page.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PageErrorCode implements ErrorCode {


    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"페이지를 찾을 수 없습니다."),
    CANNOT_INVITE_TO_PERSONAL_PAGE(HttpStatus.FORBIDDEN, "개인 페이지에는 초대를 보낼 수 없습니다."),
    CANNOT_LEAVE_PERSONAL_PAGE(HttpStatus.FORBIDDEN, "개인 페이지에서는 탈퇴할 수 없습니다."),
    /* CANNOT_ACCEPT_ALREADY_REJECTED_INVITATION(HttpStatus.BAD_REQUEST, "이미 거절된 초대 요청은 처리할 수 없습니다."),
    CANNOT_ACCEPT_ALREADY_ACCEPTED_INVITATION(HttpStatus.BAD_REQUEST, "이미 수락된 초대 요청은 재수락할 수 없습니다."),
 */   ALREADY_JOINED_SHARED_PAGE(HttpStatus.BAD_REQUEST, "이미 참여한 공유 페이지입니다.");

    ;



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
