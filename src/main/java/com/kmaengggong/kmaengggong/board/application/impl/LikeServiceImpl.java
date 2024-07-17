package com.kmaengggong.kmaengggong.board.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.LikeService;
import com.kmaengggong.kmaengggong.board.domain.LikeRecord;
import com.kmaengggong.kmaengggong.board.domain.LikeRecordRepository;
import com.kmaengggong.kmaengggong.board.domain.Likeable;
import com.kmaengggong.kmaengggong.common.exception.AlreadyLikedException;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRecordRepository likeRecordRepository;

    @Override
    public void like(Likeable entity, Long memberId) {
        if(!likeRecordRepository.existsByEntityIdAndEntityTypeAndMemberId(entity.getId(), entity.getType(), memberId)){
            LikeRecord like = LikeRecord.builder()
                .entityId(entity.getId())
                .entityType(entity.getType())
                .memberId(memberId)
                .build();
            likeRecordRepository.save(like);
        }
        else throw new AlreadyLikedException("Duplicated like");
    }

    @Override
    public int getLikes(Likeable entity) {
        return likeRecordRepository.countByEntityIdAndEntityType(entity.getId(), entity.getType());
    }
}
