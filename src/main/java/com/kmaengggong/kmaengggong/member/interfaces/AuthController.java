package com.kmaengggong.kmaengggong.member.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmaengggong.kmaengggong.common.interfaces.CommonController;
import com.kmaengggong.kmaengggong.member.application.AuthService;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthRequest;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController extends CommonController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.signIn(authRequest);
        return ResponseEntity.ok(authResponse);
    }
}
