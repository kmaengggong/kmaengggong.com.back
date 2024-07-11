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
public class MemberSaveDTO {
    private String email;
    private String password;
    private String nickname;

    public static Member toEntity(MemberSaveDTO memberSaveDTO) {
        return Member.builder()
            .email(memberSaveDTO.getEmail())
            .password(memberSaveDTO.getPassword())
            .nickname(memberSaveDTO.getNickname())
            .build();
    }
}
