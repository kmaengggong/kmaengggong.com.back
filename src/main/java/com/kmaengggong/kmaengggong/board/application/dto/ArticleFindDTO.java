package com.kmaengggong.kmaengggong.board.application.dto;

import java.time.LocalDateTime;

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
public class ArticleFindDTO {
	private Long articleId;
	private Long authorId;
	private String title;
	private String content;
	private String headerImage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ArticleFindDTO toDTO(Article article) {
		return ArticleFindDTO.builder()
			.articleId(article.getArticleId())
			.authorId(article.getAuthorId())
			.title(article.getTitle())
			.content(article.getContent())
			.headerImage(article.getHeaderImage())
			.createdAt(article.getCreatedAt())
			.updatedAt(article.getUpdatedAt())
			.build();
	}

	public static Article toEntity(ArticleFindDTO articleFindDTO) {
		return Article.builder()
			.articleId(articleFindDTO.getArticleId())
			.authorId(articleFindDTO.getAuthorId())
			.title(articleFindDTO.getTitle())
			.content(articleFindDTO.getContent())
			.headerImage(articleFindDTO.getHeaderImage())
			.createdAt(articleFindDTO.getCreatedAt())
			.updatedAt(articleFindDTO.getUpdatedAt())
			.build();
	}
}
