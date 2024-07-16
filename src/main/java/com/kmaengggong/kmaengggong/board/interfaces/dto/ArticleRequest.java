package com.kmaengggong.kmaengggong.board.interfaces.dto;

import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;

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
public class ArticleRequest {
	private Long authorId;
	private String title;
	private String content;
	private String headerIamge;

	public static ArticleSaveDTO toSaveDTO(ArticleRequest articleRequest) {
		return ArticleSaveDTO.builder()
			.authorId(articleRequest.getAuthorId())
			.title(articleRequest.getTitle())
			.content(articleRequest.getContent())
			.headerImage(articleRequest.getHeaderIamge())
			.build();
	}

	public static ArticleUpdateDTO toUpdateDTO(ArticleRequest articleRequest) {
		return ArticleUpdateDTO.builder()
			.title(articleRequest.getTitle())
			.content(articleRequest.getContent())
			.headerImage(articleRequest.getHeaderIamge())
			.build();
	}
}
