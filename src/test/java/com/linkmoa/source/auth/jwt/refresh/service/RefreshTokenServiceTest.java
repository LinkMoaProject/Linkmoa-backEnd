/*
package com.linkmoa.source.auth.jwt.refresh.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@Slf4j
public class RefreshTokenServiceTest {

    final String email = "test email";
    final String token = "token";

    @Autowired
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    @DisplayName("refresh token 조회")
    void saveRefersh(){
        refreshTokenService.saveRefreshToken(email,token);
    }


    @Test
    @DisplayName("refreshToken 조회")
    public void getRefreshToken(){
        //when
        String findToken = refreshTokenService.getRefreshToken(email);

        //than
        log.info("refreshToken test = {}",findToken);
        Assertions.assertThat(token).isEqualTo(findToken);

    }
}*/
