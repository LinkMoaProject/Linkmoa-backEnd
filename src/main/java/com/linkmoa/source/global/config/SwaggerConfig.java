package com.linkmoa.source.global.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import com.linkmoa.source.global.swagger.ExampleHolder;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SwaggerConfig {

	@Bean
	public OperationCustomizer customize() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			ApiErrorCodeExamples apiErrorCodeExample =
				handlerMethod.getMethodAnnotation(ApiErrorCodeExamples.class);
			// ApiErrorCodeExample 어노테이션 단 메소드 적용
			if (apiErrorCodeExample != null) {
				generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
			}
			return operation;
		};

	}

	/**
	 * public enum SiteErrorCode implements ErrorCode {
	 *     SITE_NOT_FOUND(HttpStatus.NOT_FOUND, "SITE_001", "Site를 찾을 수 없습니다.");
	 * }
	 * 이 Enum을 사용하여 generateErrorCodeResponseExample 메서드를 호출하면, 다음과 같은 과정이 진행됩니다:
	 *
	 * errorCodes 배열에는 SITE_NOT_FOUND가 포함됩니다.
	 * ExampleHolder 객체를 생성하여 SITE_NOT_FOUND의 상태 코드(404), 에러 코드(SITE_001), 그리고 예시 응답(getSwaggerExample(SITE_NOT_FOUND))을 설정합니다.
	 * 상태 코드(404)를 키로 하고, ExampleHolder 객체를 값으로 가지는 맵을 생성합니다.
	 * 이 맵을 ApiResponses 객체에 추가하여 API 문서에 응답 예시를 포함시킵니다.
	 */

	private void generateErrorCodeResponseExample(Operation operation, Class<? extends ErrorCode> value) {
		ApiResponses responses = operation.getResponses();

		ErrorCode[] errorCodes = value.getEnumConstants();

		Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
			.map(
				errorCode -> ExampleHolder.builder()
					.holder(getSwaggerExample((Enum<?>)errorCode)) // Swagger의 Example 객체 생성
					.name(((Enum<?>)errorCode).name()) // 에러 코드 이름
					.code(errorCode.getHttpStatus().value()) // HTTP 상태 코드
					.build()
			)
			.collect(Collectors.groupingBy(ExampleHolder::getCode));

		// ExampleHolders를 ApiResponses에 추가
		addExamplesToResponses(responses, statusWithExampleHolders);
	}

	// exampleHolder를 ApiResponses에 추가
	private void addExamplesToResponses(ApiResponses responses,
		Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
		statusWithExampleHolders.forEach(
			(status, v) -> {
				Content content = new Content();
				MediaType mediaType = new MediaType();
				ApiResponse apiResponse = new ApiResponse();

				v.forEach(
					exampleHolder -> mediaType.addExamples(
						exampleHolder.getName(),
						exampleHolder.getHolder()
					)
				);
				content.addMediaType("application/json", mediaType);
				apiResponse.setContent(content);
				responses.addApiResponse(String.valueOf(status), apiResponse);
			}
		);
	}

	private Example getSwaggerExample(Enum<?> errorCodeEnum) {
		// Enum을 ErrorCode로 캐스팅
		ErrorCode errorCode = (ErrorCode)errorCodeEnum;

		// ResponseError 객체 생성
		ApiResponseSpec apiResponseErrorSpec = ApiResponseSpec.builder()
			.status(errorCode.getHttpStatus())
			.message(errorCode.getErrorMessage())
			.build();

		Example example = new Example();
		example.setValue(apiResponseErrorSpec);

		return example;

	}

}
