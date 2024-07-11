package com.kmaengggong.kmaengggong.member.interfaces;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberSaveDTO;
import com.kmaengggong.kmaengggong.member.application.dto.MemberUpdateDTO;
import com.kmaengggong.kmaengggong.member.application.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody MemberRequest memberRequest, UriComponentsBuilder ucb) {
        System.out.println(memberRequest);
        MemberSaveDTO memberSaveDTO = MemberRequest.toSaveDto(memberRequest);
        System.out.println(memberSaveDTO);
        MemberFindDTO memberFindDTO = memberService.save(memberSaveDTO);
        System.out.println(memberFindDTO);
        URI uri = ucb
            .path("member/{memberId}")
            .buildAndExpand(memberFindDTO.getMemberId())
            .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll(Pageable pageable) {
        Page<MemberFindDTO> memberPage = memberService.findAll(pageable);
        List<MemberResponse> memberRseponseList = memberPage.getContent().stream()
            .map(MemberResponse::toResponse)
            .collect(Collectors.toList());
        Page<MemberResponse> memberResponsePage = new PageImpl<>(
            memberRseponseList, pageable, memberPage.getTotalElements());
        return ResponseEntity.ok(memberResponsePage.getContent());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findById(@PathVariable("memberId") Long memberId) {
        MemberFindDTO MemberFindDTO = memberService.findById(memberId);
        MemberResponse memberResponse = MemberResponse.toResponse(MemberFindDTO);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> update(
        @PathVariable("memberId") Long memberId,
        @RequestBody MemberRequest memberRequest
    ) {

        MemberUpdateDTO memberUpdateDTO = MemberRequest.toUpdateDto(memberRequest);
        memberService.update(memberUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteById(@PathVariable("memberId") Long memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
