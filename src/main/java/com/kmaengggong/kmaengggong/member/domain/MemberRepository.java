package com.kmaengggong.kmaengggong.member.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	List<Member> findAll();
	Page<Member> findAll(Pageable pageable);
	Optional<Member> findByEmail(String email);
	Optional<Member> findByNickname(String nickname);
	
	boolean existsByMemberId(Long memberId);
}
