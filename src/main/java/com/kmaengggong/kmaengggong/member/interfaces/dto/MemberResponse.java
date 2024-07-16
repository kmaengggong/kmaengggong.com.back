package com.kmaengggong.kmaengggong.member.interfaces.dto;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;

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
