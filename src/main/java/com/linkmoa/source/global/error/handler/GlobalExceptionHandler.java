package com.linkmoa.source.global.error.handler;


import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.global.error.dto.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
                .errorMessage(e.getMemberErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(SiteException.class)
    public ResponseEntity<?> handleSiteException(SiteException e){
        ResponseError responseError = ResponseError.builder()
                .httpStatusCode(e.getSiteErrorCode().getHttpStatus())
                .errorMessage(e.getSiteErrorCode().getErrorMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }





}
