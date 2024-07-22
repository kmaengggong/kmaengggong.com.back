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
	private String nickname;
	private String title;
	private String content;
	private String headerImage;
	private Long viewCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ArticleFindDTO toDTO(Article article, String nickname) {
		return ArticleFindDTO.builder()
			.articleId(article.getArticleId())
			.authorId(article.getAuthorId())
			.nickname(nickname)
			.viewCount(article.getViewCount())
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
			.viewCount(articleFindDTO.getViewCount())
			.title(articleFindDTO.getTitle())
			.content(articleFindDTO.getContent())
			.headerImage(articleFindDTO.getHeaderImage())
			.createdAt(articleFindDTO.getCreatedAt())
			.updatedAt(articleFindDTO.getUpdatedAt())
			.build();
	}
}
