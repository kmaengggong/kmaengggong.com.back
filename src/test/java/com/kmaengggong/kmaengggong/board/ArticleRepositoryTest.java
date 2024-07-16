package com.kmaengggong.kmaengggong.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.kmaengggong.kmaengggong.board.domain.Article;
import com.kmaengggong.kmaengggong.board.domain.ArticleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {
	@Autowired
	private ArticleRepository articleRepository;

	private Article article;

	private Long authorId = 1L;
	private String title = "title";
	private String content = "content";
	private String headerImage = "headerImage";

	@BeforeEach
	void setUp() {
		article = Article.builder()
			.authorId(authorId)
			.title(title)
			.content(content)
			.headerImage(headerImage)
			.build();
	}

	@Test
	@DisplayName("C: save")
	void saveTest() {
		// When
		Article savedArticle = articleRepository.save(article);

		// Then
		assertThat(savedArticle).isNotNull();
		assertThat(savedArticle.getArticleId()).isGreaterThan(0);
	}

	@Test
	@DisplayName("R: findAll")
	void findAllTest() {
		// Given
		Long authorId1 = 2L;
		String title1 = "title1";
		String content1 = "content1";
		String headerImage1 = "headerImage1";
		Article article1 = Article.builder()
			.authorId(authorId1)
			.title(title1)
			.content(content1)
			.headerImage(headerImage1)
			.build();

		articleRepository.save(article);
		articleRepository.save(article1);

		// When
		List<Article> articles = articleRepository.findAll();

		// Then
		assertThat(articles).isNotNull();
		assertThat(articles.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("R: findAllPage")
	void findAllPageTest() {
		// Given
		Long authorId1 = 2L;
		String title1 = "title1";
		String content1 = "content1";
		String headerImage1 = "headerImage1";
		Article article1 = Article.builder()
			.authorId(authorId1)
			.title(title1)
			.content(content1)
			.headerImage(headerImage1)
			.build();

		articleRepository.save(article);
		articleRepository.save(article1);

		// When
		Page<Article> articlePage = articleRepository.findAll(PageRequest.of(0, 10));

		// Then
		assertThat(articlePage).isNotNull();
		assertThat(articlePage.getTotalElements()).isEqualTo(2);
		assertThat(articlePage.getTotalPages()).isEqualTo(1);
		assertThat(articlePage.getSize()).isEqualTo(10);
		assertThat(articlePage.getContent()).hasSize(2);
	}

	@Test
	@DisplayName("R: findById")
	void findByIdTest() {
		// Given
		articleRepository.save(article);

		// When
		Article findArticle = articleRepository.findById(article.getArticleId()).orElse(null);

		// Then
		assertThat(findArticle).isNotNull();
		assertThat(findArticle.getArticleId()).isEqualTo(article.getArticleId());
		assertThat(findArticle.getAuthorId()).isEqualTo(article.getAuthorId());
		assertThat(findArticle.getTitle()).isEqualTo(article.getTitle());
		assertThat(findArticle.getContent()).isEqualTo(article.getContent());
		assertThat(findArticle.getHeaderImage()).isEqualTo(article.getHeaderImage());
	}

	@Test
	@DisplayName("U: update")
	void updateTest() {
		// Given
		String updateTitle = "updateTitle";
		String updateContent = "updateContent";
		String updateHeaderIamge = "updateHeaderImage";
		articleRepository.save(article);

		// When
		Article savedArticle = articleRepository.findById(article.getArticleId()).orElse(null);
		assertThat(savedArticle).isNotNull();

		savedArticle.update(updateTitle, updateContent, updateHeaderIamge);

		Article updatedArticle = articleRepository.save(savedArticle);

		// Then
		assertThat(updatedArticle).isNotNull();
		assertThat(updatedArticle.getTitle()).isEqualTo(updateTitle);
		assertThat(updatedArticle.getContent()).isEqualTo(updateContent);
		assertThat(updatedArticle.getHeaderImage()).isEqualTo(updateHeaderIamge);
	}

	@Test
	@DisplayName("D: deleteById")
	void deleteByIdTest() {
		// Given
		articleRepository.save(article);

		// When
		articleRepository.deleteById(article.getArticleId());
		Article deletedArticle = articleRepository.findById(article.getArticleId()).orElse(null);

		// Then
		assertThat(deletedArticle).isNull();
	}
}
