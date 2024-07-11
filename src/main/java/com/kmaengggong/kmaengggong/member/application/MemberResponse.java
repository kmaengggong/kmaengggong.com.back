package com.kmaengggong.kmaengggong.member.application;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberFindDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MemberResponse {
    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime registeredAt;

    public static MemberResponse toResponse(MemberFindDTO memberFindDTO) {
        return MemberResponse.builder()
            .memberId(memberFindDTO.getMemberId())
            .email(memberFindDTO.getEmail())
            .nickname(memberFindDTO.getNickname())
            .registeredAt(memberFindDTO.getRegisteredAt())
            .build();
    }
}
