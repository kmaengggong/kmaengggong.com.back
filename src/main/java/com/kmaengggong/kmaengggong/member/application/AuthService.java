package com.kmaengggong.kmaengggong.member.application;

import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.interfaces.dto.AuthRequest;

public interface AuthService {
    Member signIn(AuthRequest authRequest);
    Member getByCredentials(String userEmail);
}
