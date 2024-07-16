package com.kmaengggong.kmaengggong.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kmaengggong.kmaengggong.board.application.CommentService;
import com.kmaengggong.kmaengggong.board.application.dto.CommentFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentUpdateDTO;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    private CommentSaveDTO commentSaveDTO;

	private Long authorId = 1L;
	private Long articleId = 99L;
	private String content = "content";

    @BeforeEach
    void setUp() {
        commentSaveDTO = CommentSaveDTO.builder()
            .authorId(authorId)
            .articleId(articleId)
            .content(content)
            .build();
    }

    @Test
    @DisplayName("CR: saveAndFindLatest")
    void saveAndFindLatestTest() {
        // When
        commentService.save(commentSaveDTO);
        CommentFindDTO savedComment = commentService.findLatest();

        // Then
		assertThat(savedComment).isNotNull();
		assertThat(savedComment.getCommentId()).isGreaterThan(0);
		assertThat(savedComment.getAuthorId()).isEqualTo(commentSaveDTO.getAuthorId());
		assertThat(savedComment.getArticleId()).isEqualTo(commentSaveDTO.getArticleId());
		assertThat(savedComment.getContent()).isEqualTo(commentSaveDTO.getContent());
    }

    @Test
    @DisplayName("R: findAll")
    void findAllTest() {
        // Given
        Long authorId1 = 2L;
		Long articleId1 = 98L;
		String content1 = "content1";
		CommentSaveDTO commentSaveDTO1= CommentSaveDTO.builder()
			.authorId(authorId1)
			.articleId(articleId1)
			.content(content1)
			.build();
		
        // When
        commentService.save(commentSaveDTO);
		commentService.save(commentSaveDTO1);
        List<CommentFindDTO> comments = commentService.findAll();
        List<String> expectedCommentContents = Arrays.asList(
            commentSaveDTO.getContent(), commentSaveDTO1.getContent());

        // Then
		assertThat(comments).isNotNull();
		assertThat(comments.size()).isEqualTo(2);
		assertThat(comments.stream().map(CommentFindDTO::getContent))
			.containsExactlyInAnyOrderElementsOf(expectedCommentContents);
    }

	@Test
	@DisplayName("findAllByArticleId")
	void findAllByArticleIdTest() {
		// Given
		Long authorId1 = 2L;
		Long articleId1 = 98L;
		String content1 = "content1";
		CommentSaveDTO commentSaveDTO1 = CommentSaveDTO.builder()
			.authorId(authorId1)
			.articleId(articleId1)
			.content(content1)
			.build();
		
		// When
        commentService.save(commentSaveDTO);
		commentService.save(commentSaveDTO1);
		List<CommentFindDTO> comments = commentService.findAllByArticleId(articleId);
		
		// Then
		assertThat(comments).isNotNull();
		assertThat(comments.size()).isEqualTo(1);
		assertThat(comments.get(0).getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("U: update")
	void updateTest() {
		// Given
		String updateContent = "updateContent";

		// When
        commentService.save(commentSaveDTO);
		CommentFindDTO savedComment = commentService.findLatest();
        assertThat(savedComment).isNotNull();

		CommentUpdateDTO updateComment = CommentUpdateDTO.builder()
			.commentId(savedComment.getCommentId())
			.content(updateContent)
			.build();

		commentService.update(updateComment);
		CommentFindDTO updatedComment = commentService.findById(savedComment.getCommentId());

		// Then
		assertThat(updatedComment).isNotNull();
		assertThat(updatedComment.getContent()).isEqualTo(updateContent);
	}

	@Test
	@DisplayName("D: deleteById")
	void deleteByIdTest() {
		// When
        commentService.save(commentSaveDTO);
		CommentFindDTO savedComment = commentService.findLatest();
        assertThat(savedComment).isNotNull();

		commentService.deleteById(savedComment.getCommentId());

        // Then
        assertThrows(ResourceNotFoundException.class,
            () -> commentService.findById(savedComment.getCommentId()));
	}
}
