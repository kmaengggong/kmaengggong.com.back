package com.kmaengggong.kmaengggong.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberFindDTO;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

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
        Member savedMember = memberService.save(member);

        // Then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("R: findAll")
    void findAllTest() {
        // Given
        String email1 = "email1@email1.com";
        String password1 = "password1";
        String nickname1 = "nickname1";
        Member member1 = Member.builder()
            .email(email1)
            .password(password1)
            .nickname(nickname1)
            .build();

        memberService.save(member);
        memberService.save(member1);

        // When
        List<Member> memberList = memberService.findAll();

        // Then
        assertThat(memberList).isNotNull();
        assertThat(memberList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("R: findById")
    void findByIdTest() {
        // Given
        memberService.save(member);

        // When
        MemberFindDTO findMember = memberService.findById(member.getMemberId());

        // Then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("R: findByEmail")
    void findByEmailTest() {
        // Given
        memberService.save(member);

        // When
        Member findMember = memberService.findByEmail(member.getEmail());

        // Then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("R: findByNickname")
    void findByNicknameTest() {
        // Given
        memberService.save(member);

        // When
        Member findMember = memberService.findByNickname(member.getNickname());

        // Then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("U: update")
    void updateTest() {
        // Given
        String updateNickname = "updateNickname";
        memberService.save(member);

        // When
        MemberFindDTO savedMemberFindDTO = memberService.findById(member.getMemberId());
        Member savedMember = MemberFindDTO.toEntity(savedMemberFindDTO);
        assertThat(savedMember).isNotNull();

        savedMember.update(updateNickname);

        Member updatedMember = memberService.save(savedMember);

        // Then
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getNickname()).isEqualTo(updateNickname);
    }

    @Test
    @DisplayName("U: updatedPassword")
    void updatePasswordTest() {
        // Given
        String updatePassword = "updatePassword";
        memberService.save(member);

        // When
        MemberFindDTO savedMemberFindDTO = memberService.findById(member.getMemberId());
        Member savedMember = MemberFindDTO.toEntity(savedMemberFindDTO);
        assertThat(savedMember).isNotNull();

        savedMember.updatePassword(updatePassword);

        Member updatedMember = memberService.save(savedMember);

        // Then
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getPassword()).isEqualTo(updatePassword);
    }

    @Test
    @DisplayName("D: deleteById")
    void deleteByIdTest() {
        // Given
        memberService.save(member);

        // When
        memberService.deleteById(member.getMemberId());
        
        MemberFindDTO deletedMemberFindDTO = memberService.findById(member.getMemberId());
        Member deletedMember = MemberFindDTO.toEntity(deletedMemberFindDTO);

        // Then
        assertThat(deletedMember).isNull();
    }
}
