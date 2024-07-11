package com.kmaengggong.kmaengggong.member.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmaengggong.kmaengggong.member.application.MemberResponse;
import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberFindDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findById(@PathVariable Long memberId) {
        MemberFindDTO MemberFindDTO = memberService.findById(memberId);
        if(MemberFindDTO == null){
            return ResponseEntity.notFound().build();
        }
        else{
            MemberResponse memberResponse = MemberResponse.toResponse(MemberFindDTO);
            return ResponseEntity.ok(memberResponse);
        }
    }
}
