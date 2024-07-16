package com.kmaengggong.kmaengggong.board.domain;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
	void save(@Param("comment") Comment comment);
	List<Comment> findAll();
	List<Comment> findAllByArticleId(@Param("articleId") Long articleId);
	Comment findById(@Param("commentId") Long commentId);
	Comment findLatest();
	void update(@Param("comment") Comment comment);
	void deleteById(@Param("commentId") Long commentId);
}
