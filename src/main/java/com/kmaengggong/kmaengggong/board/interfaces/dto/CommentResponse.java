package com.kmaengggong.kmaengggong.board.interfaces.dto;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.board.application.dto.CommentFindDTO;

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
public class CommentResponse {
	private Long commentId;
	private Long authorId;
	private Long articleId;
	private String content;
	private LocalDateTime createdAt;

	public static CommentResponse toResponse(CommentFindDTO commentFindDTO) {
		return CommentResponse.builder()
			.commentId(commentFindDTO.getCommentId())
			.authorId(commentFindDTO.getAuthorId())
			.articleId(commentFindDTO.getArticleId())
			.content(commentFindDTO.getContent())
			.createdAt(commentFindDTO.getCreatedAt())
			.build();
	}
}
