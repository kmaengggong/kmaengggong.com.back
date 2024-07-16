package com.kmaengggong.kmaengggong.board.application.dto;

import java.time.LocalDateTime;

import com.kmaengggong.kmaengggong.board.domain.Comment;

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
public class CommentSaveDTO {
	private Long authorId;
	private Long articleId;
	private String content;
	private Long likeCount;
	private LocalDateTime createdAt;

	public static Comment toEntity(CommentSaveDTO commentSaveDTO) {
		return Comment.builder()
			.authorId(commentSaveDTO.getAuthorId())
			.articleId(commentSaveDTO.getArticleId())
			.content(commentSaveDTO.getContent())
			.likeCount(commentSaveDTO.getLikeCount())
			.createdAt(commentSaveDTO.getCreatedAt())
			.build();
	}
}
