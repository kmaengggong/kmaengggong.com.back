package com.kmaengggong.kmaengggong.board.application;

import com.kmaengggong.kmaengggong.board.domain.Likeable;

public interface LikeService {
    void like(Likeable entity, Long memberId);
    int getLikes(Likeable entity);
}
