package com.kmaengggong.kmaengggong.member.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmaengggong.kmaengggong.common.interfaces.CommonController;
import com.kmaengggong.kmaengggong.jwt.application.TokenService;
import com.kmaengggong.kmaengggong.jwt.interfaces.dto.AccessTokenResponse;
import com.kmaengggong.kmaengggong.member.application.AuthService;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController extends CommonController {
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/signin")
    public ResponseEntity<AccessTokenResponse> signIn(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody AuthRequest authRequest) {

        Member member = authService.signIn(authRequest);
        if(member == null) return ResponseEntity.badRequest().build();
        String accessToken = tokenService.createNewAccessTokenAndRefreshToken(request, response, member);
        AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder()
            .accessToken(accessToken)
            .build();

        return ResponseEntity.ok(accessTokenResponse);
    }
}
