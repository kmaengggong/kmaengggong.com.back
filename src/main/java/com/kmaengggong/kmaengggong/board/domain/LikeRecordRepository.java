package com.kmaengggong.kmaengggong.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long> {
    boolean existsByEntityIdAndEntityTypeAndMemberId(Long entityId, String entityType, Long memberId);
    int countByEntityIdAndEntityType(Long entityId, String entityType);
}
