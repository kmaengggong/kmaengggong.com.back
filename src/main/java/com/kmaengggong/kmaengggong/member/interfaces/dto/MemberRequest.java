package com.kmaengggong.kmaengggong.member.interfaces.dto;

import com.kmaengggong.kmaengggong.member.application.dto.MemberSaveDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdateDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdatePasswordDTO;

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
public class MemberRequest {
	private String email;
	private String password;
	private String nickname;

	public static MemberSaveDTO toSaveDto(MemberRequest memberRequest) {
		return MemberSaveDTO.builder()
			.email(memberRequest.getEmail())
			.password(memberRequest.getPassword())
			.nickname(memberRequest.getNickname())
			.build();
	}

	public static MemberUpdateDTO toUpdateDto(MemberRequest memberRequest) {
		return MemberUpdateDTO.builder()
			.nickname(memberRequest.getNickname())
			.build();
	}

	public static MemberUpdatePasswordDTO toUpdatePasswordDto(MemberRequest memberRequest) {
		return MemberUpdatePasswordDTO.builder()
			.password(memberRequest.getPassword())
			.build();
	}
}
