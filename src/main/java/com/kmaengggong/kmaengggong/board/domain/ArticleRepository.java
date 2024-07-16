package com.kmaengggong.kmaengggong.board.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findAll();
	Page<Article> findAll(Pageable pageable);
	
	boolean existsByArticleId(Long articleId);
}
