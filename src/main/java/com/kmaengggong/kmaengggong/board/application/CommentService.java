package com.kmaengggong.kmaengggong.board.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.dto.CommentFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentUpdateDTO;

@Service
public interface CommentService {
	void save(CommentSaveDTO commentSaveDTO);
	List<CommentFindDTO> findAll();
	List<CommentFindDTO> findAllByArticleId(Long articleId);
	CommentFindDTO findById(Long commentId);
	CommentFindDTO findLatest();
	void update(CommentUpdateDTO commentUpdateDTO);
	void deleteById(Long commentId);
}
