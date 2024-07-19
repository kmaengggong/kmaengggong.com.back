package com.kmaengggong.kmaengggong.board.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.CommentService;
import com.kmaengggong.kmaengggong.board.application.dto.CommentFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentUpdateDTO;
import com.kmaengggong.kmaengggong.board.domain.Comment;
import com.kmaengggong.kmaengggong.board.domain.CommentMapper;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentMapper commentMapper;

	@Override
	@Transactional
	public void save(CommentSaveDTO commentSaveDTO) {
		Comment comment = CommentSaveDTO.toEntity(commentSaveDTO);
		commentMapper.save(comment);
	}
	
	@Override
	public List<CommentFindDTO> findAll() {
		List<Comment> comments = commentMapper.findAll();
		return comments.stream()
			.map(CommentFindDTO::toDTO)
			.collect(Collectors.toList());
	}

	@Override
	public List<CommentFindDTO> findAllByArticleId(Long articleId) {
		List<Comment> comments = commentMapper.findAllByArticleId(articleId);
		return comments.stream()
			.map(CommentFindDTO::toDTO)
			.collect(Collectors.toList());
	}

	@Override
	public CommentFindDTO findById(Long commentId) {
		Comment comment = commentMapper.findById(commentId);
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		return CommentFindDTO.toDTO(comment);
	}

	@Override
	public CommentFindDTO findLatest() {
		Comment comment = commentMapper.findLatest();
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		return CommentFindDTO.toDTO(comment);
	}

	@Override
	@Transactional
	public void update(CommentUpdateDTO commentUpdateDTO) {
		Comment comment = commentMapper.findById(commentUpdateDTO.getCommentId());
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		Comment updateComment = CommentUpdateDTO.toEntity(commentUpdateDTO);
		commentMapper.update(updateComment);
	}

	@Override
	@Transactional
	public void deleteById(Long commentId) {
		Comment comment = commentMapper.findById(commentId);
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		commentMapper.deleteById(commentId);
	}
}