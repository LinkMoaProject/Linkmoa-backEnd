package com.linkmoa.source.global.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    //String getErrorCode();
    String getErrorMessage();
}
