package com.kmaengggong.kmaengggong.member.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.domain.Member;

@Service
public interface MemberService {
    void save(Member member);

    List<Member> findAll();
    Member findById(Long memberId);

    void update(Member member);

    void updatePassword(Member member);

    void deleteById(Long memeberId);
}
