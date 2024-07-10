package com.kmaengggong.kmaengggong.member;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

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
        
        memberRepository.save(member);
        memberRepository.save(member1);

        // When
        List<Member> memberList = memberRepository.findAll();

        // Then
        assertThat(memberList).isNotNull();
        assertThat(memberList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("R: findById")
    void findByIdTest() {
        // Given
        memberRepository.save(member);

        // When
        Member findMember = memberRepository.findById(member.getMemberId()).orElse(null);

        // Then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
    }

    // findByEmail
    @Test
    @DisplayName("R: findByEmail")
    void findByEmailTest() {
        // Given
        memberRepository.save(member);

        // When
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);

        // Then
        assertThat(findMember).isNotNull();
    }

    // findByNickname
    @Test
    @DisplayName("R: findByNickname")
    void findByNicknameTest() {
        // Given
        memberRepository.save(member);

        // When
        Member findMember = memberRepository.findByNickname(member.getNickname()).orElse(null);

        // Then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("U: update")
    void updateTest() {
        // Given
        String updateNickname = "updateNickname";
        memberRepository.save(member);

        // When
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
        memberRepository.save(member);

        // When
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
        // Given
        memberRepository.save(member);

        // When
        memberRepository.deleteById(member.getMemberId());
        Member deletedMember = memberRepository.findById(member.getMemberId()).orElse(null);

        // Then
        assertThat(deletedMember).isNull();
    }
}
