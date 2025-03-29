package com.linkmoa.source.global.error.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.global.exception.ValidationException;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<?> handleMemberException(MemberException e) {
		ApiResponseSpec memberExceptionResponse = ApiResponseSpec.builder()
			.status(e.getMemberErrorCode().getHttpStatus())
			.message(e.getMemberErrorCode().getErrorMessage())
			.build();
		return ResponseEntity.status(e.getMemberErrorCode().getHttpStatus()).body(memberExceptionResponse);
	}

	@ExceptionHandler(SiteException.class)
	public ResponseEntity<?> handleSiteException(SiteException e) {
		ApiResponseSpec siteExceptionResponse = ApiResponseSpec.builder()
			.status(e.getSiteErrorCode().getHttpStatus())
			.message(e.getSiteErrorCode().getErrorMessage())
			.build();

		return ResponseEntity.status(e.getSiteErrorCode().getHttpStatus()).body(siteExceptionResponse);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException e) {
		ApiResponseSpec validationExceptionResponse = ApiResponseSpec.builder()
			.status(e.getValidationErrorCode().getHttpStatus())
			.message(e.getValidationErrorCode().getErrorMessage())
			.build();
		return ResponseEntity.status(e.getValidationErrorCode().getHttpStatus()).body(validationExceptionResponse);
	}

	@ExceptionHandler(PageException.class)
	public ResponseEntity<?> handleValidationException(PageException e) {
		ApiResponseSpec pageExceptionResponse = ApiResponseSpec.builder()
			.status(e.getPageErrorCode().getHttpStatus())
			.message(e.getPageErrorCode().getErrorMessage())
			.build();
		return ResponseEntity.status(e.getPageErrorCode().getHttpStatus()).body(pageExceptionResponse);
	}

	@ExceptionHandler(DispatchException.class)
	public ResponseEntity<?> handleDispatchException(DispatchException e) {
		ApiResponseSpec dispatchExceptionResponse = ApiResponseSpec.builder()
			.status(e.getErrorCode().getHttpStatus())
			.message(e.getErrorCode().getErrorMessage())
			.build();
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(dispatchExceptionResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception e) {
		log.error("예기치 못한 오류가 발생했습니다", e);

		ApiResponseSpec apiResponseSpec = ApiResponseSpec.builder()
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.message("서버에서 예상치 못한 오류가 발생했습니다.")
			.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseSpec);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseSpec<Map<String, String>>> handleValidationException(
		MethodArgumentNotValidException ex) {
		Map<String, String> errors = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponseSpec.fail(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", errors));
	}

}
