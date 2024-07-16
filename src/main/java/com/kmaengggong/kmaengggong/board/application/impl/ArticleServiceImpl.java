package com.kmaengggong.kmaengggong.board.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.ArticleService;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;
import com.kmaengggong.kmaengggong.board.domain.Article;
import com.kmaengggong.kmaengggong.board.domain.ArticleRepository;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
	private final ArticleRepository articleRepository;

	@Override
	@Transactional
	public ArticleFindDTO save(ArticleSaveDTO articleSaveDTO) {
		Article article = ArticleSaveDTO.toEntity(articleSaveDTO);
		article = articleRepository.save(article);
		return ArticleFindDTO.toDTO(article);
	}

	@Override
	public List<ArticleFindDTO> findAll() {
		List<Article> articles = articleRepository.findAll();
		return articles.stream()
			.map(ArticleFindDTO::toDTO)
			.collect(Collectors.toList());
	}

	@Override
	public Page<ArticleFindDTO> findAll(Pageable pageable) {
		// 0 이하의 페이지 -> 0으로
		int pageNumber = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber();
		// 조회
		Page<Article> articlePage = articleRepository.findAll(PageRequest.of(
			pageNumber,
			pageable.getPageSize(),
			pageable.getSort()
		));
		// 0페이지보다 큰데 비어있다면, 그 전의 마지막 페이지로 다시 조회
		if(articlePage.isEmpty() && pageNumber > 0){
			int lastPage = articlePage.getTotalPages() - 1;
			pageNumber = lastPage > 0 ? lastPage : 0;
			articlePage = articleRepository.findAll(PageRequest.of(
				pageNumber,
				pageable.getPageSize(),
				pageable.getSort()
			));
		}

		List<ArticleFindDTO> articleFindDTOs = articlePage.getContent().stream()
			.map(ArticleFindDTO::toDTO)
			.collect(Collectors.toList());
		return new PageImpl<>(articleFindDTOs, pageable, articlePage.getTotalElements());
	}

	@Override
	public ArticleFindDTO findById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		return ArticleFindDTO.toDTO(article);
	}

	@Override
	@Transactional
	public void update(ArticleUpdateDTO articleUpdateDTO) {
		Article article = articleRepository.findById(articleUpdateDTO.getArticleId())
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		article.update(
			articleUpdateDTO.getTitle(),
			articleUpdateDTO.getContent(),
			articleUpdateDTO.getHeaderImage()
		);
		articleRepository.save(article);
	}

	@Override
	@Transactional
	public void deleteById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		articleRepository.deleteById(article.getArticleId());
	}
}
