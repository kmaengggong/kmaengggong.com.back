package com.kmaengggong.kmaengggong.member.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;
import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberFindDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService { 
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public MemberFindDTO findById(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        if(findMember == null) throw new NullPointerException();
        MemberFindDTO memberFindDTO = MemberFindDTO.toDTO(findMember);

        return memberFindDTO;
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElse(null);
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
