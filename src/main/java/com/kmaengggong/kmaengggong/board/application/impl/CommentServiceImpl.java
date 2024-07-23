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
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentMapper commentMapper;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public void save(CommentSaveDTO commentSaveDTO) {
		Comment comment = CommentSaveDTO.toEntity(commentSaveDTO);
		commentMapper.save(comment);
	}
	
	@Override
	public List<CommentFindDTO> findAll() {
		List<Comment> comments = commentMapper.findAll();
		List<CommentFindDTO> commentFindDTOs = comments.stream()
			.map((comment) -> CommentFindDTO.toDTO(comment, null))
			.collect(Collectors.toList());
			return buildCommentHierarchy(commentFindDTOs).stream()
			.map((comment) -> getNickname(comment))
			.collect(Collectors.toList());
	}

	@Override
	public List<CommentFindDTO> findAllByArticleId(Long articleId) {
		List<Comment> comments = commentMapper.findAllByArticleId(articleId);
		List<CommentFindDTO> commentFindDTOs = comments.stream()
			.map((comment) -> CommentFindDTO.toDTO(comment, null))
			.collect(Collectors.toList());
		return buildCommentHierarchy(commentFindDTOs).stream()
			.map((comment) -> getNickname(comment))
			.collect(Collectors.toList());
	}

	@Override
	public CommentFindDTO findById(Long commentId) {
		Comment comment = commentMapper.findById(commentId);
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		return getNickname(CommentFindDTO.toDTO(comment, null));
	}

	@Override
	public CommentFindDTO findLatest() {
		Comment comment = commentMapper.findLatest();
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		return getNickname(CommentFindDTO.toDTO(comment, null));
	}

	@Override
	@Transactional
	public void update(CommentUpdateDTO commentUpdateDTO) {
		Comment comment = commentMapper.findById(commentUpdateDTO.getCommentId());
		if(comment == null) throw new ResourceNotFoundException("Comment not found");
		if(commentUpdateDTO.getContent() == null) return;
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

	@Override
    public int countCommentHierarchy(Long articleId) {
        List<Comment> comments = commentMapper.findAllByArticleId(articleId);
        return countTotalComments(comments);
    }

	private List<CommentFindDTO> buildCommentHierarchy(List<CommentFindDTO> commentFindDTOs) {
		return commentFindDTOs.stream()
			.filter((commentFindDTO) -> commentFindDTO.getParentId() == null)
			.map((commentFindDTO) -> {
				commentFindDTO.setReplies(findReplies(commentFindDTO, commentFindDTOs));
				return commentFindDTO;
			})
			.collect(Collectors.toList());
	}

	private List<CommentFindDTO> findReplies(CommentFindDTO parentComment, List<CommentFindDTO> commentFindDTOs) {
		return commentFindDTOs.stream()
			.filter((commentFindDTO) -> parentComment.getCommentId().equals(commentFindDTO.getParentId()))
			.map((commentFindDTO) -> {
				commentFindDTO.setReplies(findReplies(commentFindDTO, commentFindDTOs));
				commentFindDTO = getNickname(commentFindDTO);
				return commentFindDTO;
			})
			.collect(Collectors.toList());
	}

    private int countTotalComments(List<Comment> comments) {
		if(comments == null) return 0;
        int count = comments.size();
        for (Comment comment : comments) {
            count += countTotalComments(comment.getReplies());
        }
        return count;
    }

	private CommentFindDTO getNickname(CommentFindDTO commentFindDTO) {
		Member member = memberRepository.findById(commentFindDTO.getAuthorId()).orElse(null);
		String nickname = (member != null) ? member.getNickname() : "Unknown";
		commentFindDTO.setNickname(nickname);
		return commentFindDTO;
	}
}
