package com.kmaengggong.kmaengggong.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
// @RequiredArgsConstructor
public class SecurityConfig {

    // 정적 파일, .jsp 미적용
    @Bean
    WebSecurityCustomizer configure() {
        return web -> web.ignoring()
            .requestMatchers("/static/**")
            .dispatcherTypeMatchers(DispatcherType.FORWARD);
    }

    // http 요청
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorizationConfig -> {
                authorizationConfig
                    .requestMatchers("/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated();
            })
            .formLogin(formLoginConfig -> {  // 토큰 기반 로그인 시 폼 로그인 막음
                formLoginConfig
                    .disable();
            })
            .httpBasic(httpBasicConfig -> {
                httpBasicConfig
                    .disable();
            })
            .logout(logoutConfig -> {
                logoutConfig
                    .logoutUrl("/signout")
                    .logoutSuccessUrl("/signin")
                    .invalidateHttpSession(true);
            })
            .sessionManagement(sessionConfig -> {
                sessionConfig
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .csrf(csrfConfig -> {
                csrfConfig
                    .disable();
            })
//				.oauth2Login(oauth2Config -> {
//					oauth2Config.loginPage("/signin")
//						.authorizationEndpoint(endpointConfig ->
//							endpointConfig
//								.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
//						.successHandler(oAuth2SuccessHandler())
//						.userInfoEndpoint(userInfoConfig ->
//								.userInfoConfig
//									.userService(oauth2UserCustomService));
//				})
            // Request를 서버가 처리하기 직전 시점에 해당 필터를 사용해 로그인 검증
            // .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // 암호화 모듈 임포트
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	// 토큰 인증 필터
	// @Bean
	// TokenAuthenticationFilter tokenAuthenticationFilter() {
	// 	return new TokenAuthenticationFilter(tokenProvider);
	// }
}
