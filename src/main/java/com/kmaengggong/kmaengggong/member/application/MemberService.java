package com.kmaengggong.kmaengggong.member.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberSaveDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdateDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdatePasswordDTO;

@Service
public interface MemberService {
    MemberFindDTO save(MemberSaveDTO memberSaveDTO);

    List<MemberFindDTO> findAll();
    Page<MemberFindDTO> findAll(Pageable pageable);
    MemberFindDTO findById(Long memberId);
    MemberFindDTO findByEmail(String email);
    MemberFindDTO findByNickname(String nickname);

    void update(MemberUpdateDTO memberUpdateDTO);

    void updatePassword(MemberUpdatePasswordDTO MemberUpdatePasswordDTO);

    void deleteById(Long memberId);

    boolean passwordCheck(Long memberId, String password);
}
