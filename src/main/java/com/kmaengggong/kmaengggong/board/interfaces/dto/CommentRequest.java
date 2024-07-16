package com.kmaengggong.kmaengggong.board.interfaces.dto;

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
	private Long authorId;
	private String title;
	private String content;
	private String headerImage;
}
