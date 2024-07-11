package com.kmaengggong.kmaengggong.member.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberFindDTO;

@Service
public interface MemberService {
    Member save(Member member);

    List<Member> findAll();
    MemberFindDTO findById(Long memberId);
    Member findByEmail(String email);
    Member findByNickname(String nickname);

    void update(Member member);

    void updatePassword(Member member);

    void deleteById(Long memeberId);
}
