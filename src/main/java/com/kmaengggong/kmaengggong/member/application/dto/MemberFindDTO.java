package com.kmaengggong.kmaengggong.member.application.dto;

import java.time.LocalDateTime;

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
public class MemberFindDTO {
	private Long memberId;
	private String email;
	private String nickname;
	private LocalDateTime registeredAt;
	private String refreshToken;

	public static MemberFindDTO toDTO(Member member) {
		return MemberFindDTO.builder()
			.memberId(member.getMemberId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.registeredAt(member.getRegisteredAt())
			.refreshToken(member.getRefreshToken())
			.build();
	}

	public static Member toEntity(MemberFindDTO memberFindDTO) {
		return Member.builder()
			.memberId(memberFindDTO.getMemberId())
			.email(memberFindDTO.getEmail())
			.nickname(memberFindDTO.getNickname())
			.registeredAt(memberFindDTO.getRegisteredAt())
			.refreshToken(memberFindDTO.getRefreshToken())
			.build();
	}
}
