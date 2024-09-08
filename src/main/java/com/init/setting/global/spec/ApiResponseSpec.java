package com.init.setting.global.spec;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseSpec {

    private Boolean status; // true or false
    private int code; // 200, 401, 404 ...
    private String message; // 왜 에러가 발생했는지
}
