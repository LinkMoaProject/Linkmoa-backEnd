package com.linkmoa.source.global.config;

import com.linkmoa.source.auth.jwt.filter.JwtAuthorizationFilter;
import com.linkmoa.source.auth.oauth2.handler.CustomOauth2SuccessHandler;
import com.linkmoa.source.auth.oauth2.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(corsCustomizer ->corsCustomizer.configurationSource(corsConfigurationSource()))
                        .csrf(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/members/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/jwt/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll() // OAuth 관련 경로 허용
                                .anyRequest().authenticated()
                        )
                        .oauth2Login((oauth2) -> oauth2
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                                .successHandler(customOauth2SuccessHandler))
                        //.addFilterAfter(customJsonUserPasswordAuthenticationFilter(), LogoutFilter.class)
                        .addFilterBefore(jwtAuthorizationFilter,LogoutFilter.class)
                        //.addFilterBefore(jwtExceptionHandlerFilter, JwtAuthorizationFilter.class)
                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://linkmoa-front.vercel.app" // 배포된 프론트엔드 도메인 추가
        ));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Collections.singletonList("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); //1시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
