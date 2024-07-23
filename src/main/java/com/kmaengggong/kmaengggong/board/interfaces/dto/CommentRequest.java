package com.kmaengggong.kmaengggong.board.interfaces.dto;

import com.kmaengggong.kmaengggong.board.application.dto.CommentSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentUpdateDTO;

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
public class CommentRequest {
	private Long parentId;
	private Long authorId;
	private Long articleId;
	private String content;

	public static CommentSaveDTO toSaveDTO(CommentRequest commentRequest) {
		return CommentSaveDTO.builder()
			.parentId(commentRequest.getParentId())
			.authorId(commentRequest.getAuthorId())
			.articleId(commentRequest.getArticleId())
			.content(commentRequest.getContent())
			.build();
	}

	public static CommentUpdateDTO toUpdateDTO(CommentRequest commentRequest) {
		return CommentUpdateDTO.builder()
			.content(commentRequest.getContent())
			.build();
	}
}
