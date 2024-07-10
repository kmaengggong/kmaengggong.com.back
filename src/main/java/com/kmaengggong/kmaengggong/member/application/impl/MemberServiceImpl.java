package com.kmaengggong.kmaengggong.member.application.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{
    @Autowired
    private final MemberRepository memberRepository;

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public void update(Member member) {
        Member findMember = memberRepository.findById(member.getMemberId()).orElse(null);
        if(findMember == null) throw new NullPointerException();

        findMember.update(member.getNickname());

        memberRepository.save(findMember);
    }

    @Override
    public void updatePassword(Member member) {
        Member findMember = memberRepository.findById(member.getMemberId()).orElse(null);
        if(findMember == null) throw new NullPointerException();

        findMember.updatePassword(member.getPassword());

        memberRepository.save(findMember);
    }
    
    @Override
    public void deleteById(Long memeberId) {
        memberRepository.deleteById(memeberId);
    }
}
