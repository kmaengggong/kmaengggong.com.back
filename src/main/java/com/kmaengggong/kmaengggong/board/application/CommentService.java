package com.kmaengggong.kmaengggong.board.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.domain.Comment;

@Service
public interface CommentService {
	List<Comment> findAll();
	List<Comment> findAllByArticleId(Long articleId);
	Comment findById(Long commentId);
	void save(Comment comment);
	void update(Comment comment);
	void delete(Long commentId);
}
