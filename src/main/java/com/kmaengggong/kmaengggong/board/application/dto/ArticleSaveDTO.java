package com.kmaengggong.kmaengggong.board.application.dto;

import com.kmaengggong.kmaengggong.board.domain.Article;

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
public class ArticleSaveDTO {
    private Long authorId;
    private String title;
    private String content;
    private String headerImage;

    public static Article toEntity(ArticleSaveDTO articleSaveDTO) {
        return Article.builder()
            .authorId(articleSaveDTO.getAuthorId())
            .title(articleSaveDTO.getTitle())
            .content(articleSaveDTO.getContent())
            .headerImage(articleSaveDTO.getHeaderImage())
            .build();
    }
}
