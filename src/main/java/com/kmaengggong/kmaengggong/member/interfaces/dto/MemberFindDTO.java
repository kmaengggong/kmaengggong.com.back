package com.kmaengggong.kmaengggong.member.interfaces.dto;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.member.domain.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MemberFindDTO {
    private Long memberId;
    private String email;
    private String nickname;
    private LocalDateTime registeredAt;

    public static MemberFindDTO toDTO(Member member) {
        return MemberFindDTO.builder()
            .memberId(member.getMemberId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .registeredAt(member.getRegisteredAt())
            .build();
    }

    public static Member toEntity(MemberFindDTO memberFindDTO) {
        return Member.builder()
            .memberId(memberFindDTO.getMemberId())
            .email(memberFindDTO.getEmail())
            .nickname(memberFindDTO.getNickname())
            .registeredAt(memberFindDTO.getRegisteredAt())
            .build();
    }
}
