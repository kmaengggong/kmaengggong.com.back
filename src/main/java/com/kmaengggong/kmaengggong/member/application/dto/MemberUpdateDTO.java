package com.kmaengggong.kmaengggong.member.application.dto;

import com.kmaengggong.kmaengggong.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {
    private Long memberId;
    private String nickname;

    public static Member toEntity(MemberUpdateDTO memberUpdateDTO) {
        return Member.builder()
            .memberId(memberUpdateDTO.getMemberId())
            .nickname(memberUpdateDTO.getNickname())
            .build();
    }
}
