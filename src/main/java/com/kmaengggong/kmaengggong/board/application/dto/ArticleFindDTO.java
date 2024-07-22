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
	private String categoryName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ArticleFindDTO toDTO(Article article, String nickname, String categoryName) {
		return ArticleFindDTO.builder()
			.articleId(article.getArticleId())
			.authorId(article.getAuthorId())
			.nickname(nickname)
			.title(article.getTitle())
			.content(article.getContent())
			.viewCount(article.getViewCount())
			.categoryName(categoryName)
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
			.viewCount(articleFindDTO.getViewCount())
			.createdAt(articleFindDTO.getCreatedAt())
			.updatedAt(articleFindDTO.getUpdatedAt())
			.build();
	}
}
