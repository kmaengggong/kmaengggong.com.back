package com.kmaengggong.kmaengggong.board.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findAll();
	Page<Article> findAllByIsDeletedIsFalse(Pageable pageable);
	List<Article> findAllByIsDeletedIsTrue();
	
	boolean existsByArticleId(Long articleId);
}
