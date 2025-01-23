package com.linkmoa.source.domain.dispatch.error;


import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DispatchErrorCode implements ErrorCode {

    TRANSMIT_DIRECTORY_REQUEST_ALREADY_EXIST(HttpStatus.BAD_REQUEST , "이미 해당 디렉토리 전송 요청을 전송하였습니다."),
    TRANSMIT_DIRECTORY_REQUEST_ACCEPTED_EXIST(HttpStatus.BAD_REQUEST , "이미 해당 디렉토리 전송 요청을 수락하였습니다."),
    SHARE_PAGE_INVITATION_REQUEST_ALREADY_EXIST(HttpStatus.BAD_REQUEST , "이미 해당 페이지 초대 요청을 전송하였습니다."),
    SHARE_PAGE_INVITATION_REQUEST_ACCEPTED_EXIST(HttpStatus.BAD_REQUEST , "이미 해당 페이지 초대 요청을 수락하였습니다."),
    REQUEST_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST , "이미 처리된 요청입니다."),
    INVALID_NOTIFICATION_TYPE(HttpStatus.BAD_REQUEST, "잘못된 알림 유형입니다."),
    INVALID_RECEIVER(HttpStatus.BAD_REQUEST, "요청 처리 권한이 없습니다. 요청 수신자만 요청을 처리할 수 있습니다"),
    SHARE_PAGE_INVITATION_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "공유 페이지 초대 요청을 찾을 수 없습니다."),
    TRANSMIT_DIRECTORY_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "디렉토리 전송 요청을 찾을 수 없습니다.");


    private HttpStatus httpStatus;
    private String errorMessage;

    DispatchErrorCode(HttpStatus httpStatus, String errorMessage) {
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
