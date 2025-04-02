package com.linkmoa.source.global.error.handler;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.global.error.code.impl.ErrorCategory;
import com.linkmoa.source.global.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String code, String uri) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
		problem.setTitle(title);
		problem.setType(URI.create(code));
		problem.setInstance(URI.create(uri));
		problem.setProperty("errorCode", code);
		return problem;
	}

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ProblemDetail> handleMemberException(MemberException e, HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(buildProblemDetail(
				e.getErrorCode().getHttpStatus(),
				ErrorCategory.MEMBER.message(),
				e.getErrorCode().getErrorMessage(),
				e.getErrorCode().name(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(SiteException.class)
	public ResponseEntity<ProblemDetail> handleSiteException(SiteException e, HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(buildProblemDetail(
				e.getErrorCode().getHttpStatus(),
				ErrorCategory.SITE.message(),
				e.getErrorCode().getErrorMessage(),
				e.getErrorCode().name(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ProblemDetail> handleValidationException(ValidationException e, HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(buildProblemDetail(
				e.getErrorCode().getHttpStatus(),
				ErrorCategory.BUSINESS.message(),
				e.getErrorCode().getErrorMessage(),
				e.getErrorCode().name(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(PageException.class)
	public ResponseEntity<ProblemDetail> handleValidationException(PageException e, HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(buildProblemDetail(
				e.getErrorCode().getHttpStatus(),
				ErrorCategory.PAGE.message(),
				e.getErrorCode().getErrorMessage(),
				e.getErrorCode().name(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(DispatchException.class)
	public ResponseEntity<ProblemDetail> handleDispatchException(DispatchException e, HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(buildProblemDetail(
				e.getErrorCode().getHttpStatus(),
				ErrorCategory.DISPATCH.message(),
				e.getErrorCode().getErrorMessage(),
				e.getErrorCode().name(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleAllExceptions(Exception e, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(buildProblemDetail(
				HttpStatus.INTERNAL_SERVER_ERROR,
				ErrorCategory.SYSTEM.message(),
				"서버에서 예상치 못한 오류가 발생했습니다.",
				"INTERNAL_SERVER_ERROR",
				request.getRequestURI()
			));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException e,
		HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(buildProblemDetail(
				HttpStatus.BAD_REQUEST,
				ErrorCategory.ILLEGAL_ARGUMENT.message(),
				e.getMessage(),
				"ILLEGAL_ARGUMENT",
				request.getRequestURI()
			));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpServletRequest request) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		ProblemDetail problem = buildProblemDetail(
			HttpStatus.BAD_REQUEST,
			ErrorCategory.VALIDATION.message(),
			"입력값이 올바르지 않습니다.",
			"VALIDATION_FAILED",
			request.getRequestURI()
		);
		problem.setProperty("errors", errors);
		return ResponseEntity.badRequest().body(problem);
	}

}
