package com.kmaengggong.kmaengggong.member.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        // 0 이하의 페이지 -> 0으로
        int pageNumber = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber();
        // 조회
        Page<Member> memberPage = memberRepository.findAll(PageRequest.of(
            pageNumber,
            pageable.getPageSize(),
            pageable.getSort()
        ));
        // 0페이지보다 큰데 비어있다면, 그 전의 마지막 페이지로 다시 조회
        if(memberPage.isEmpty() && pageNumber > 0){
            int lastPage = memberPage.getTotalPages() - 1;
            pageNumber = lastPage > 0 ? lastPage : 0;
            memberPage = memberRepository.findAll(PageRequest.of(
                pageNumber,
                pageable.getPageSize(),
                pageable.getSort()
            ));
        }
        
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
