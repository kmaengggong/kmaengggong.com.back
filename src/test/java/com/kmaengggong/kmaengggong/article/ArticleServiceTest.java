package com.kmaengggong.kmaengggong.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.kmaengggong.kmaengggong.article.application.ArticleService;
import com.kmaengggong.kmaengggong.article.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.article.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.article.application.dto.ArticleUpdateDTO;
import com.kmaengggong.kmaengggong.spring.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    private ArticleFindDTO articleFindDTO;
    private ArticleSaveDTO articleSaveDTO;

    private Long authorId = 1L;
    private String title = "title";
    private String content = "content";
    private String headerImage = "headerImage";

    @BeforeEach
    void setUp() {
        articleSaveDTO = ArticleSaveDTO.builder()
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
        articleFindDTO = articleService.save(articleSaveDTO);

        // Then
        assertThat(articleFindDTO).isNotNull();
        assertThat(articleFindDTO.getArticleId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("R: findAll")
    void findAllTest(){
        // Given
        Long authorId1 = 2L;
        String title1 = "title1";
        String content1 = "content1";
        String headerImage1 = "headerImage1";
        ArticleSaveDTO articleSaveDTO1 = ArticleSaveDTO.builder()
            .authorId(authorId1)
            .title(title1)
            .content(content1)
            .headerImage(headerImage1)
            .build();

        articleService.save(articleSaveDTO);
        articleService.save(articleSaveDTO1);

        // When
        List<ArticleFindDTO> articleList = articleService.findAll();

        // Then
        assertThat(articleList).isNotNull();
        assertThat(articleList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("R: findAllPage")
    void findAllPageTest(){
        // Given
        Long authorId1 = 2L;
        String title1 = "title1";
        String content1 = "content1";
        String headerImage1 = "headerImage1";
        ArticleSaveDTO articleSaveDTO1 = ArticleSaveDTO.builder()
            .authorId(authorId1)
            .title(title1)
            .content(content1)
            .headerImage(headerImage1)
            .build();

        articleService.save(articleSaveDTO);
        articleService.save(articleSaveDTO1);

        // When
        Page<ArticleFindDTO> articlePage = articleService.findAll(PageRequest.of(0, 10));

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
        articleFindDTO = articleService.save(articleSaveDTO);

        // When
        ArticleFindDTO foundArticle = articleService.findById(articleFindDTO.getArticleId());

        // Then
        assertThat(foundArticle).isNotNull();
        assertThat(foundArticle.getArticleId()).isEqualTo(articleFindDTO.getArticleId());
        assertThat(foundArticle.getAuthorId()).isEqualTo(articleFindDTO.getAuthorId());
        assertThat(foundArticle.getTitle()).isEqualTo(articleFindDTO.getTitle());
        assertThat(foundArticle.getContent()).isEqualTo(articleFindDTO.getContent());
        assertThat(foundArticle.getHeaderImage()).isEqualTo(articleFindDTO.getHeaderImage());
    }

    @Test
    @DisplayName("U: update")
    void updateTest() {
        // Given
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateHeaderImage = "updateHeaderImage";
        articleFindDTO = articleService.save(articleSaveDTO);

        // When
        ArticleFindDTO savedArticleFindDTO = articleService.findById(articleFindDTO.getArticleId());
        assertThat(savedArticleFindDTO).isNotNull();

        ArticleUpdateDTO articleUpdateDTO = ArticleUpdateDTO.builder()
            .articleId(savedArticleFindDTO.getArticleId())
            .title(updateTitle)
            .content(updateContent)
            .headerImage(updateHeaderImage)
            .build();
        articleService.update(articleUpdateDTO);

        ArticleFindDTO updatedArticleFindDTO = articleService.findById(articleUpdateDTO.getArticleId());

        // Then
        assertThat(updatedArticleFindDTO).isNotNull();
        assertThat(updatedArticleFindDTO.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedArticleFindDTO.getContent()).isEqualTo(updateContent);
        assertThat(updatedArticleFindDTO.getHeaderImage()).isEqualTo(updateHeaderImage);
    }

    @Test
    @DisplayName("D: deleteById")
    void deleteByIdTest() {
        // Given
        articleFindDTO = articleService.save(articleSaveDTO);

        // When
        articleService.deleteById(articleFindDTO.getArticleId());

        // Then
        assertThrows(ResourceNotFoundException.class,
            () -> articleService.findById(articleFindDTO.getArticleId()));
    }
}
