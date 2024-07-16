package com.kmaengggong.kmaengggong.board.application.dto;

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
public class CommentUpdateDTO {
	private Long commentId;
	private String content;

	public static Comment toEntity(CommentUpdateDTO commentUpdateDTO) {
		return Comment.builder()
			.commentId(commentUpdateDTO.getCommentId())
			.content(commentUpdateDTO.getContent())
			.build();
	}
}
