package com.kmaengggong.kmaengggong.board.interfaces.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
	private List<CommentResponse> replies;

	public static CommentResponse toResponse(CommentFindDTO commentFindDTO) {
		return CommentResponse.builder()
			.commentId(commentFindDTO.getCommentId())
			.authorId(commentFindDTO.getAuthorId())
			.articleId(commentFindDTO.getArticleId())
			.content(commentFindDTO.getContent())
			.createdAt(commentFindDTO.getCreatedAt())
			.replies(commentFindDTO.getReplies().stream()
				.map(CommentResponse::toResponse)
				.collect(Collectors.toList())
			)
			.build();
	}
}
