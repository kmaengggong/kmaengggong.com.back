package com.kmaengggong.kmaengggong.board.interfaces.dto;

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
	private String nickname;
	private String title;
	private String content;
	private String headerImage;
	private Long viewCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ArticleResponse toResponse(ArticleFindDTO articleFindDTO) {
		return ArticleResponse.builder()
			.articleId(articleFindDTO.getArticleId())
			.authorId(articleFindDTO.getAuthorId())
			.nickname(articleFindDTO.getNickname())
			.title(articleFindDTO.getTitle())
			.content(articleFindDTO.getContent())
			.headerImage(articleFindDTO.getHeaderImage())
			.viewCount(articleFindDTO.getViewCount())
			.createdAt(articleFindDTO.getCreatedAt())
			.updatedAt(articleFindDTO.getUpdatedAt())
			.build();
	}
}
