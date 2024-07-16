package com.kmaengggong.kmaengggong.board.interfaces.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    private ArticleRequest articleRequest;
    private List<CommentRequest> commentRequests;
}
