package com.linkmoa.source.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getErrorCode();
}
