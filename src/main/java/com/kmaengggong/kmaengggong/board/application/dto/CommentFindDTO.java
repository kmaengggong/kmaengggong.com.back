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
	private Long parentId;
	private Long authorId;
	private String nickname;
	private Long articleId;
	private String content;
	private LocalDateTime createdAt;
	private List<CommentFindDTO> replies;
	private boolean isDeleted;

	public static CommentFindDTO toDTO(Comment comment, String nickname) {
		return CommentFindDTO.builder()
			.commentId(comment.getCommentId())
			.authorId(comment.getAuthorId())
			.parentId(comment.getParentId())
			.nickname(nickname)
			.articleId(comment.getArticleId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.replies(comment.getReplies() == null ? null : comment.getReplies().stream()
				.map((c) -> CommentFindDTO.toDTO(c, nickname))
				.collect(Collectors.toList())
			)
			.isDeleted(comment.isDeleted())
			.build();
	}

	public static Comment toEntity(CommentFindDTO commentFindDTO) {
		return Comment.builder()
			.commentId(commentFindDTO.getCommentId())
			.authorId(commentFindDTO.getAuthorId())
			.parentId(commentFindDTO.getParentId())
			.articleId(commentFindDTO.getArticleId())
			.content(commentFindDTO.getContent())
			.createdAt(commentFindDTO.getCreatedAt())
			.isDeleted(commentFindDTO.isDeleted())
			.build();
	}
}
