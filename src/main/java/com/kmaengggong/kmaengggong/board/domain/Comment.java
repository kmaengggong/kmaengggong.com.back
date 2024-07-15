package com.kmaengggong.kmaengggong.board.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long commentId;
    private Long authorId;
    private Long articleId;
    private String content;
    private Long likeCount;
    private LocalDateTime createdAt;

    public void updateLike() {
        this.likeCount += 1;
    }
}
