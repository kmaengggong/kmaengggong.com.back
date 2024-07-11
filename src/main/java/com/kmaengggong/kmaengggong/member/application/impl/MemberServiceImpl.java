package com.kmaengggong.kmaengggong.member.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberSaveDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdateDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdatePasswordDTO;
import com.kmaengggong.kmaengggong.member.application.exception.ResourceNotFoundException;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService { 
    private final MemberRepository memberRepository;

    @Override
    public MemberFindDTO save(MemberSaveDTO memberSaveDTO) {
        Member member = MemberSaveDTO.toEntity(memberSaveDTO);
        member = memberRepository.save(member);
        return MemberFindDTO.toDTO(member);
    }

    @Override
    public List<MemberFindDTO> findAll() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream()
            .map(MemberFindDTO::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<MemberFindDTO> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        List<MemberFindDTO> memberFindDTOList = memberPage.getContent().stream()
            .map(MemberFindDTO::toDTO)
            .collect(Collectors.toList());
        return new PageImpl<>(memberFindDTOList, pageable, memberPage.getTotalElements());
    }

    @Override
    public MemberFindDTO findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return MemberFindDTO.toDTO(member);
    }

    @Override
    public MemberFindDTO findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return MemberFindDTO.toDTO(member);
    }

    @Override
    public MemberFindDTO findByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return MemberFindDTO.toDTO(member);
    }

    @Override
    public void update(MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findById(memberUpdateDTO.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.update(memberUpdateDTO.getNickname());
        memberRepository.save(member);
    }

    @Override
    public void updatePassword(MemberUpdatePasswordDTO memberUpdatePasswordDTO) {
        Member member = memberRepository.findById(memberUpdatePasswordDTO.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.updatePassword(memberUpdatePasswordDTO.getPassword());
        memberRepository.save(member);
    }
    
    @Override
    public void deleteById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        memberRepository.deleteById(member.getMemberId());
    }

    @Override
    public boolean passwordCheck(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        if(member.getPassword().equals(password)) return true;
        else return false;
    }
}
