package com.kmaengggong.kmaengggong.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberSaveDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdateDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdatePasswordDTO;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	private MemberFindDTO memberFindDTO;
	private MemberSaveDTO memberSaveDTO;
	
	private String email = "email@email.com";
	private String password = "password";
	private String nickname = "nickname";

	@BeforeEach
	void setUp() {
		memberSaveDTO = MemberSaveDTO.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}

	@Test
	@DisplayName("C: save")
	void saveTest() {
		// When
		memberFindDTO = memberService.save(memberSaveDTO);

		// Then
		assertThat(memberFindDTO).isNotNull();
		assertThat(memberFindDTO.getMemberId()).isGreaterThan(0);
	}

	@Test
	@DisplayName("R: findAll")
	void findAllTest() {
		// Given
		String email1 = "email1@email1.com";
		String password1 = "password1";
		String nickname1 = "nickname1";
		MemberSaveDTO memberSaveDTO1 = MemberSaveDTO.builder()
			.email(email1)
			.password(password1)
			.nickname(nickname1)
			.build();

		// When
		memberService.save(memberSaveDTO);
		memberService.save(memberSaveDTO1);
		List<MemberFindDTO> members = memberService.findAll();

		// Then
		assertThat(members).isNotNull();
		assertThat(members.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("R: findAllPage")
	void findAllPageTest() {
		// Given
		String email1 = "email1@email1.com";
		String password1 = "password1";
		String nickname1 = "nickname1";
		MemberSaveDTO memberSaveDTO1 = MemberSaveDTO.builder()
			.email(email1)
			.password(password1)
			.nickname(nickname1)
			.build();

		// When
		memberService.save(memberSaveDTO);
		memberService.save(memberSaveDTO1);
		Page<MemberFindDTO> memberPage = memberService.findAll(PageRequest.of(0, 10));

		// Then
		assertThat(memberPage).isNotNull();
		assertThat(memberPage.getTotalElements()).isEqualTo(2);
		assertThat(memberPage.getTotalPages()).isEqualTo(1);
		assertThat(memberPage.getSize()).isEqualTo(10);
		assertThat(memberPage.getContent()).hasSize(2);
	}

	@Test
	@DisplayName("R: findById")
	void findByIdTest() {
		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		MemberFindDTO foundMember = memberService.findById(memberFindDTO.getMemberId());

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(memberFindDTO.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(memberSaveDTO.getEmail());
		assertThat(foundMember.getNickname()).isEqualTo(memberSaveDTO.getNickname());
	}

	@Test
	@DisplayName("R: findByEmail")
	void findByEmailTest() {
		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		MemberFindDTO foundMember = memberService.findByEmail(memberFindDTO.getEmail());

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(memberFindDTO.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(memberSaveDTO.getEmail());
		assertThat(foundMember.getNickname()).isEqualTo(memberSaveDTO.getNickname());
	}

	@Test
	@DisplayName("R: findByNickname")
	void findByNicknameTest() {
		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		MemberFindDTO foundMember = memberService.findByNickname(memberFindDTO.getNickname());

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(memberFindDTO.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(memberSaveDTO.getEmail());
		assertThat(foundMember.getNickname()).isEqualTo(memberSaveDTO.getNickname());
	}

	@Test
	@DisplayName("U: update")
	void updateTest() {
		// Given
		String updateNickname = "updateNickname";

		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		MemberFindDTO savedMemberFindDTO = memberService.findById(memberFindDTO.getMemberId());
		assertThat(savedMemberFindDTO).isNotNull();

		MemberUpdateDTO memberUpdateDTO = MemberUpdateDTO.builder()
			.memberId(savedMemberFindDTO.getMemberId())
			.nickname(updateNickname)
			.build();
		memberService.update(memberUpdateDTO);

		MemberFindDTO updatedMemberFindDTO = memberService.findById(memberUpdateDTO.getMemberId());

		// Then
		assertThat(updatedMemberFindDTO).isNotNull();
		assertThat(updatedMemberFindDTO.getNickname()).isEqualTo(updateNickname);
	}

	@Test
	@DisplayName("U: updatedPassword")
	void updatePasswordTest() {
		// Given
		String updatePassword = "updatePassword";

		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		MemberFindDTO savedMemberFindDTO = memberService.findById(memberFindDTO.getMemberId());
		assertThat(savedMemberFindDTO).isNotNull();

		MemberUpdatePasswordDTO memberUpdatePasswordDTO = MemberUpdatePasswordDTO.builder()
			.memberId(savedMemberFindDTO.getMemberId())
			.password(updatePassword)
			.build();
			
		memberService.updatePassword(memberUpdatePasswordDTO);
		MemberFindDTO updatedMemberFindDTO = memberService.findById(memberUpdatePasswordDTO.getMemberId());

		// Then
		assertThat(updatedMemberFindDTO).isNotNull();
		assertThat(memberService.passwordCheck(memberFindDTO.getMemberId(), updatePassword)).isTrue();
	}

	@Test
	@DisplayName("D: deleteById")
	void deleteByIdTest() {
		// When
		memberFindDTO = memberService.save(memberSaveDTO);
		memberService.deleteById(memberFindDTO.getMemberId());
		
		// Then
		assertThrows(ResourceNotFoundException.class,
			() -> memberService.findById(memberFindDTO.getMemberId()));
	}
}
