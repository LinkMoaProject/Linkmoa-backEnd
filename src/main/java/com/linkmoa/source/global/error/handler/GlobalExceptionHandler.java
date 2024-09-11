package com.linkmoa.source.global.error.handler;


import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.global.error.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> handleMemberException(MemberException e){
        ResponseError responseError = ResponseError.builder()
                .httpStatusCode(e.getMemberErrorCode().getHttpStatus())
                .errorCode(e.getMemberErrorCode().getErrorCode())
                .errorMessage(e.getMemberErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }



}
