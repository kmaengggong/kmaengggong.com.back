package com.kmaengggong.kmaengggong.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	private String email = "email@email.com";
	private String password = "password";
	private String nickname = "nickname";

	@BeforeEach
	void setUp() {
		member = Member.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}

	@Test
	@DisplayName("C: save")
	void saveTest() {
		// When
		Member savedMember = memberRepository.save(member);

		// Then
		assertThat(savedMember).isNotNull();
		assertThat(savedMember.getMemberId()).isGreaterThan(0);
	}

	@Test
	@DisplayName("R: findAll")
	void findByAllTest() {
		// Given
		String email1 = "email1@email1.com";
		String password1 = "password1";
		String nickname1 = "nickname1";
		Member member1 = Member.builder()
			.email(email1)
			.password(password1)
			.nickname(nickname1)
			.build();
		
		// When
		memberRepository.save(member);
		memberRepository.save(member1);
		List<Member> members = memberRepository.findAll();

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
		Member member1 = Member.builder()
			.email(email1)
			.password(password1)
			.nickname(nickname1)
			.build();

		// When
		memberRepository.save(member);
		memberRepository.save(member1);
		Page<Member> memberPage = memberRepository.findAll(PageRequest.of(0, 10));

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
		memberRepository.save(member);
		Member foundMember = memberRepository.findById(member.getMemberId()).orElse(null);

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(member.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(foundMember.getPassword()).isEqualTo(member.getPassword());
		assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
	}

	// findByEmail
	@Test
	@DisplayName("R: findByEmail")
	void findByEmailTest() {
		// When
		memberRepository.save(member);
		Member foundMember = memberRepository.findByEmail(member.getEmail()).orElse(null);

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(member.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(foundMember.getPassword()).isEqualTo(member.getPassword());
		assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
	}

	// findByNickname
	@Test
	@DisplayName("R: findByNickname")
	void findByNicknameTest() {
		// When
		memberRepository.save(member);
		Member foundMember = memberRepository.findByNickname(member.getNickname()).orElse(null);

		// Then
		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getMemberId()).isEqualTo(member.getMemberId());
		assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(foundMember.getPassword()).isEqualTo(member.getPassword());
		assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
	}

	@Test
	@DisplayName("U: update")
	void updateTest() {
		// Given
		String updateNickname = "updateNickname";
		// When
		memberRepository.save(member);
		Member savedMember = memberRepository.findById(member.getMemberId()).orElse(null);
		assertThat(savedMember).isNotNull();

		savedMember.update(updateNickname);
		Member updatedMember = memberRepository.save(savedMember);

		// Then
		assertThat(updatedMember).isNotNull();
		assertThat(updatedMember.getNickname()).isEqualTo(updateNickname);
	}

	@Test
	@DisplayName("U: updatePassword")
	void updatePasswordTest() {
		// Given
		String updatePassword = "updatePassword";

		// When
		memberRepository.save(member);
		Member savedMember = memberRepository.findById(member.getMemberId()).orElse(null);
		assertThat(savedMember).isNotNull();

		savedMember.updatePassword(updatePassword);
		Member updatedMember = memberRepository.save(savedMember);

		// Then
		assertThat(updatedMember).isNotNull();
		assertThat(updatedMember.getPassword()).isEqualTo(updatePassword);
	}

	@Test
	@DisplayName("D: deleteById")
	void deleteByIdTest() {
		// When
		memberRepository.save(member);
		memberRepository.deleteById(member.getMemberId());
		Member deletedMember = memberRepository.findById(member.getMemberId()).orElse(null);

		// Then
		assertThat(deletedMember).isNull();
	}
}
