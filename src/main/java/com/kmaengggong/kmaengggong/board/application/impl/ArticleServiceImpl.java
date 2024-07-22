package com.kmaengggong.kmaengggong.board.application.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.ArticleService;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;
import com.kmaengggong.kmaengggong.board.domain.Article;
import com.kmaengggong.kmaengggong.board.domain.ArticleRepository;
import com.kmaengggong.kmaengggong.board.domain.Category;
import com.kmaengggong.kmaengggong.board.domain.CategoryRepository;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
	private static final int PAGE_SIZE = 10;
	private static final int DELETE_LIMIT = 7;

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public ArticleFindDTO save(ArticleSaveDTO articleSaveDTO) {
		Category category = categoryRepository.findById(articleSaveDTO.getCategoryId()).orElse(null);
		Article article = ArticleSaveDTO.toEntity(articleSaveDTO, category);
		article = articleRepository.save(article);
		return getNicknameAndCategoryName(article);
	}

	@Override
	public List<ArticleFindDTO> findAll() {
		List<Article> articles = articleRepository.findAll();
		return articles.stream()
			.map((article) -> {
				return getNicknameAndCategoryName(article);
			})
			.collect(Collectors.toList());
	}

	@Override
	public Page<ArticleFindDTO> findAll(Pageable pageable) {
		// // 0 이하의 페이지 -> 0으로
		// int pageNumber = pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber();
		// // 조회
		// Page<Article> articlePage = articleRepository.findAllByIsDeletedIsFalse(PageRequest.of(
		// 	pageNumber - 1,
		// 	PAGE_SIZE,
		// 	Sort.by(Sort.Direction.DESC, "createdAt")
		// ));
		// // 0페이지보다 큰데 비어있다면, 그 전의 마지막 페이지로 다시 조회
		// if(articlePage.isEmpty() && pageNumber > 0){
		// 	int lastPage = articlePage.getTotalPages() - 1;
		// 	pageNumber = lastPage > 1 ? lastPage : 1;
		// 	articlePage = articleRepository.findAllByIsDeletedIsFalse(PageRequest.of(
		// 		pageNumber - 1,
		// 		PAGE_SIZE,
		// 		Sort.by(Sort.Direction.DESC, "createdAt")
		// 	));
		// }
		int pageNumber = getCalibratedPageNum(Integer.toUnsignedLong(pageable.getPageNumber()));
		Page<Article> articlePage = articleRepository.findAllByIsDeletedIsFalse(PageRequest.of(
			pageNumber - 1,
			PAGE_SIZE,
			Sort.by(Sort.Direction.DESC, "createdAt")
		));

		Page<ArticleFindDTO> articleFindDTOs = articlePage
			.map((article) -> {
				return getNicknameAndCategoryName(article);
		});
		return articleFindDTOs;
	}

	@Override
	public ArticleFindDTO findById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		return getNicknameAndCategoryName(article);
	}

	@Override
	@Transactional
	public void update(ArticleUpdateDTO articleUpdateDTO) {
		Article article = articleRepository.findById(articleUpdateDTO.getArticleId())
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		Category category = categoryRepository.findById(articleUpdateDTO.getCategoryId()).orElse(null);
		article.update(
			articleUpdateDTO.getTitle(),
			articleUpdateDTO.getContent(),
			articleUpdateDTO.getHeaderImage(),
			category
		);
		articleRepository.save(article);
	}

	@Override
	@Transactional
	public void deleteById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		// articleRepository.deleteById(article.getArticleId());
		article.updateDeleted(true);
		articleRepository.save(article);
	}

	@Override
	@Transactional
	public void restoreById(Long articleId) {
		Article article = articleRepository.findById(articleId)
		.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		article.updateDeleted(false);
		articleRepository.save(article);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteByTime() {
		LocalDateTime now = LocalDateTime.now();
		articleRepository.findAllByIsDeletedIsTrue().stream()
			.filter((article) -> article.getDeletedAt().plusDays(DELETE_LIMIT).isBefore(now))
			.forEach((article) -> articleRepository.deleteById(article.getArticleId()));
	}

	@Override
	@Transactional
	public void incrementViewCount(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new ResourceNotFoundException("Article not found"));
		article.updateViewCount(article.getViewCount() + 1);
		articleRepository.save(article);
	}

	public ArticleFindDTO getNicknameAndCategoryName(Article article) {
		Member member = memberRepository.findById(article.getAuthorId()).orElse(null);
		String nickname = (member != null) ? member.getNickname() : "Unknown";
		String categoryName = article.getCategory().getCategoryName();
		return ArticleFindDTO.toDTO(article, nickname, categoryName);
	}

	// 보정된 pageNum으로 가공해주는 메서드
	public int getCalibratedPageNum(Long pageNum) {
		if(pageNum == null || pageNum <= 0L) {
			pageNum = 1L;
		}
		else {
			int totalPagesCount = (int) Math.ceil(articleRepository.count() / (PAGE_SIZE + 0.0));
			pageNum = pageNum > totalPagesCount ? totalPagesCount : pageNum;
		}
		return pageNum.intValue();
	}
}
