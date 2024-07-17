package com.kmaengggong.kmaengggong.board.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Likeable {
	private Long commentId;
	private Long authorId;
	private Long articleId;
	private String content;
	private LocalDateTime createdAt;

	@Override
	public Long getId() {
		return commentId;
	}

	@Override
	public String getType() {
		return "Comment";
	}
}
