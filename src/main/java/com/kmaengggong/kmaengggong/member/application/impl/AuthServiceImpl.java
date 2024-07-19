package com.kmaengggong.kmaengggong.member.application.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.common.exception.PasswordMismatchException;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;
import com.kmaengggong.kmaengggong.member.application.AuthService;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthRequest;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AuthResponse signIn(AuthRequest authRequest) {
        // 로직
        Member member = memberRepository.findByEmail(authRequest.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if(!bCryptPasswordEncoder.matches(authRequest.getPassword(), member.getPassword())){
            throw new PasswordMismatchException("Password mismatch");
        }

        // 토큰 생성 및 보내기
        return AuthResponse.builder().token("fuck").build();
    }
}
