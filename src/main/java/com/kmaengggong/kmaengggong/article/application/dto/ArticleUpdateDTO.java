package com.kmaengggong.kmaengggong.article.application.dto;

import com.kmaengggong.kmaengggong.article.domain.Article;

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
public class ArticleUpdateDTO {
    private Long articleId;
    private String title;
    private String content;
    private String headerImage;

    public static Article toEntity(ArticleUpdateDTO articleUpdateDTO) {
        return Article.builder()
            .articleId(articleUpdateDTO.getArticleId())
            .title(articleUpdateDTO.getTitle())
            .content(articleUpdateDTO.getContent())
            .headerImage(articleUpdateDTO.getHeaderImage())
            .build();
    }
}
