package com.kmaengggong.kmaengggong.board.interfaces;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;

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
public class ArticleResponse {
	private Long articleId;
	private Long authorId;
	private String title;
	private String content;
	private String headerImage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ArticleResponse toResponse(ArticleFindDTO articleFindDTO) {
		return ArticleResponse.builder()
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
