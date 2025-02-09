package com.linkmoa.source.domain.member.dto.request;

import com.linkmoa.source.domain.member.constant.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
        @NotNull
        String ageRange,
        @NotNull(message = "Gender는 필수 값입니다.")
        Gender gender,
        @NotNull @Size(max = 100, message = "Job은 최대 100자까지 가능합니다.")
        String job,

        @NotNull
        String nickName
) {
}
