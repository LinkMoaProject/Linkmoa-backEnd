package com.linkmoa.source.auth.jwt.controller;


import com.linkmoa.source.auth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtService jwtService;

   /* @GetMapping("/reissue")
    public ResponseEntity<ApiResponseSpec> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = jwtService.getRefreshTokenFromCookies(request.getCookies());

        if (jwtService.validateToken(refreshToken)) {

            Member member = jwtService.findMemberByRefresh(refreshToken);
            String accessToken = jwtService.createAccessToken(member.getEmail(), member.getRole().name());

            response.addHeader("Authorization", accessToken);
            response.sendRedirect("http://localhost:3000/mainpage");
        }

        return ResponseEntity.ok()
                .body(new ApiResponseSpec(true, 200, "Access token을 재 갱신하였습니다."));
    }*/
}
