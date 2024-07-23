package com.kmaengggong.kmaengggong.board.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
public class CommentFindDTO {
	private Long commentId;
	private Long authorId;
	private Long articleId;
	private String content;
	private LocalDateTime createdAt;
	private List<CommentFindDTO> replies;

	public static CommentFindDTO toDTO(Comment comment) {
		return CommentFindDTO.builder()
			.commentId(comment.getCommentId())
			.authorId(comment.getAuthorId())
			.articleId(comment.getArticleId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.replies(comment.getReplies().stream()
				.map(CommentFindDTO::toDTO)
				.collect(Collectors.toList())
			)
			.build();
	}

	public static Comment toEntity(CommentFindDTO commentFindDTO) {
		return Comment.builder()
			.commentId(commentFindDTO.getCommentId())
			.authorId(commentFindDTO.getAuthorId())
			.articleId(commentFindDTO.getArticleId())
			.content(commentFindDTO.getContent())
			.createdAt(commentFindDTO.getCreatedAt())
			.replies(commentFindDTO.getReplies().stream()
				.map(CommentFindDTO::toEntity)
				.collect(Collectors.toList())
			)
			.build();
	}
}
