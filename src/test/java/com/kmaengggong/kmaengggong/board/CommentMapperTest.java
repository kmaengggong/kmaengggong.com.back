package com.kmaengggong.kmaengggong.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kmaengggong.kmaengggong.board.domain.Comment;
import com.kmaengggong.kmaengggong.board.domain.CommentMapper;

@MybatisTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentMapperTest {
	@Autowired
	private CommentMapper commentMapper;

	private Comment comment;

	private Long authorId = 1L;
	private Long articleId = 99L;
	private String content = "content";

	@BeforeEach
	void setUp() {
	comment = Comment.builder()
		.authorId(authorId)
		.articleId(articleId)
		.content(content)
		.build();
	}

	@Test
	@DisplayName("CR: saveAndFindLatest")
	void saveAndFindLatestTest() {
		// When
		commentMapper.save(comment);
		Comment savedComment = commentMapper.findLatest();

		// Then
		assertThat(savedComment).isNotNull();
		assertThat(savedComment.getCommentId()).isGreaterThan(0);
		assertThat(savedComment.getAuthorId()).isEqualTo(comment.getAuthorId());
		assertThat(savedComment.getArticleId()).isEqualTo(comment.getArticleId());
		assertThat(savedComment.getContent()).isEqualTo(comment.getContent());
	}

	@Test
	@DisplayName("R: findAll")
	void findAllTest() {
		// Given
		Long authorId1 = 2L;
		Long articleId1 = 98L;
		String content1 = "content1";
		Comment comment1 = Comment.builder()
			.authorId(authorId1)
			.articleId(articleId1)
			.content(content1)
			.build();
		
		// When
		commentMapper.save(comment);
		commentMapper.save(comment1);
		List<Comment> comments = commentMapper.findAll();
		List<String> expectedCommentContents = Arrays.asList(comment.getContent(), comment1.getContent());
		
		// Then
		assertThat(comments).isNotNull();
		assertThat(comments.size()).isEqualTo(2);
		assertThat(comments.stream().map(Comment::getContent))
			.containsExactlyInAnyOrderElementsOf(expectedCommentContents);
	}

	@Test
	@DisplayName("findAllByArticleId")
	void findAllByArticleIdTest() {
		// Given
		Long authorId1 = 2L;
		Long articleId1 = 98L;
		String content1 = "content1";
		Comment comment1 = Comment.builder()
			.authorId(authorId1)
			.articleId(articleId1)
			.content(content1)
			.build();
		
		// When
		commentMapper.save(comment);
		commentMapper.save(comment1);
		List<Comment> comments = commentMapper.findAllByArticleId(articleId);
		
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
		commentMapper.save(comment);

		// When
		Comment savedComment = commentMapper.findLatest();
		assertThat(savedComment).isNotNull();

		Comment updateComment = Comment.builder()
			.commentId(savedComment.getCommentId())
			.content(updateContent)
			.build();

		commentMapper.update(updateComment);
		Comment updatedComment = commentMapper.findById(savedComment.getCommentId());

		// Then
		assertThat(updatedComment).isNotNull();
		assertThat(updatedComment.getContent()).isEqualTo(updateContent);
	}

	@Test
	@DisplayName("D: deleteById")
	void deleteByIdTest() {
		// Given
		commentMapper.save(comment);

		// When
		Comment savedComment = commentMapper.findLatest();
		assertThat(savedComment).isNotNull();

		commentMapper.deleteById(savedComment.getCommentId());
		Comment deletedComment = commentMapper.findById(savedComment.getCommentId());

		// Then
		assertThat(deletedComment).isNull();
	}
}
