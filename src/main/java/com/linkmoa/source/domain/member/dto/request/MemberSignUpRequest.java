package com.linkmoa.source.domain.member.dto.request;

import com.linkmoa.source.domain.member.constant.Sex;
import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
        @NotNull
        @Min(value = 0, message = "Age는 0 이상이어야 합니다.")
        Integer age,
        @NotNull(message = "Sex는 필수 값입니다.")
        Sex sex,
        @NotNull @Size(max = 100, message = "Job은 최대 100자까지 가능합니다.")
        String job
) {
}
