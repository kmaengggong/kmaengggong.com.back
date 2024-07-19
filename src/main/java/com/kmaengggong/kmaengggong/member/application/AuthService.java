package com.kmaengggong.kmaengggong.member.application;

import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthRequest;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthResponse;

public interface AuthService {
    AuthResponse signIn(AuthRequest authRequest);
}
