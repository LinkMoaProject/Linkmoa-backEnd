package com.linkmoa.source.auth.jwt.controller;


import com.linkmoa.source.auth.jwt.service.JwtService;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
@Slf4j
public class JwtController {

    private final JwtService jwtService;
    @GetMapping("/access-token")
    public ResponseEntity<ApiResponseSpec> getAccessToken(
            @CookieValue(value = "refresh_token") String refreshToken,
            HttpServletResponse response) throws IOException {


        String accessToken = null;

        if (jwtService.validateToken(refreshToken)) {
            String email = jwtService.getEmail(refreshToken);
            String role = jwtService.getRole(refreshToken);
            accessToken = jwtService.createAccessToken(email,role);

            response.setHeader("Authorization", accessToken);
            response.sendRedirect("http://localhost:3000/mainpage");
        }

        ApiResponseSpec apiResponseSpec =new ApiResponseSpec(HttpStatus.OK,"Access Token 발급 성공");
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(apiResponseSpec);
    }



        //log.info("token : {}", refreshToken);
        //String accessToken = jwtService.createAccessToken("

    /*@GetMapping("/reissue")
    public ResponseEntity<ApiResponseSpec> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = jwtService.getRefreshTokenFromCookies(request.getCookies());

        if (jwtService.validateToken(refreshToken)) {

            Member member = jwtService.findMemberByRefresh(refreshToken);
            String accessToken = jwtService.createAccessToken(member.getEmail(), member.getRole().name());

            response.addHeader("Authorization", accessToken);
            response.sendRedirect("http://localhost:3000/mainpage");
        }

        return ResponseEntity.ok()
                .body(new ApiResponseSpec(HttpStatus.OK, "Access token을 재 갱신하였습니다."));

    }*/
}
