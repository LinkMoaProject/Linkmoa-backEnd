package com.linkmoa.source.global.error.handler;


import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.global.exception.ValidationException;
import com.linkmoa.source.global.spec.ApiResponseErrorSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> handleMemberException(MemberException e){
        ApiResponseErrorSpec memberExceptionResponse = ApiResponseErrorSpec.builder()
                .httpStatusCode(e.getMemberErrorCode().getHttpStatus())
                .errorMessage(e.getMemberErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(e.getMemberErrorCode().getHttpStatus()).body(memberExceptionResponse);
    }

    @ExceptionHandler(SiteException.class)
    public ResponseEntity<?> handleSiteException(SiteException e){
        ApiResponseErrorSpec siteExceptionResponse = ApiResponseErrorSpec.builder()
                .httpStatusCode(e.getSiteErrorCode().getHttpStatus())
                .errorMessage(e.getSiteErrorCode().getErrorMessage())
                .build();

        return ResponseEntity.status(e.getSiteErrorCode().getHttpStatus()).body(siteExceptionResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e){
        ApiResponseErrorSpec validationExceptionResponse = ApiResponseErrorSpec.builder()
                .httpStatusCode(e.getValidationErrorCode().getHttpStatus())
                .errorMessage(e.getValidationErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(e.getValidationErrorCode().getHttpStatus()).body(validationExceptionResponse);
    }


    @ExceptionHandler(PageException.class)
    public ResponseEntity<?> handleValidationException(PageException e){
        ApiResponseErrorSpec pageExceptionResponse = ApiResponseErrorSpec.builder()
                .httpStatusCode(e.getPageErrorCode().getHttpStatus())
                .errorMessage(e.getPageErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(e.getPageErrorCode().getHttpStatus()).body(pageExceptionResponse);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception e) {
        ApiResponseErrorSpec apiResponseErrorSpec = ApiResponseErrorSpec.builder()
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage("서버에서 예상치 못한 오류가 발생했습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseErrorSpec);
    }


    @ExceptionHandler(DispatchException.class)
    public ResponseEntity<?> handleDispatchException(DispatchException e) {
        ApiResponseErrorSpec dispatchExceptionResponse = ApiResponseErrorSpec.builder()
                .httpStatusCode(e.getErrorCode().getHttpStatus())
                .errorMessage(e.getErrorCode().getErrorMessage())
                .build();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(dispatchExceptionResponse);
    }





}
