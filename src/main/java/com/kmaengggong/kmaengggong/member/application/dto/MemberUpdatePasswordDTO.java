package com.kmaengggong.kmaengggong.member.application.dto;

import com.kmaengggong.kmaengggong.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdatePasswordDTO {
	private Long memberId;
	private String password;

	public static Member toEntity(MemberUpdatePasswordDTO memberUpdatePasswordDTO) {
		return Member.builder()
			.memberId(memberUpdatePasswordDTO.getMemberId())
			.password(memberUpdatePasswordDTO.getPassword())
			.build();
	}
}
