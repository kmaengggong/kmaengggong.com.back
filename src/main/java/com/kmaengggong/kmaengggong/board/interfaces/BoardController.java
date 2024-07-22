package com.kmaengggong.kmaengggong.board.interfaces;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kmaengggong.kmaengggong.board.application.ArticleService;
import com.kmaengggong.kmaengggong.board.application.CommentService;
import com.kmaengggong.kmaengggong.board.application.LikeService;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;
import com.kmaengggong.kmaengggong.board.domain.Article;
import com.kmaengggong.kmaengggong.board.interfaces.dto.ArticleRequest;
import com.kmaengggong.kmaengggong.board.interfaces.dto.ArticleResponse;
import com.kmaengggong.kmaengggong.board.interfaces.dto.BoardResponse;
import com.kmaengggong.kmaengggong.board.interfaces.dto.CommentResponse;
import com.kmaengggong.kmaengggong.common.interfaces.CommonController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController extends CommonController {
	private final ArticleService articleService;
	private final CommentService commentService;
	private final LikeService likeService;
	private final PagedResourcesAssembler<BoardResponse> pagedResourcesAssembler;

	@PostMapping
	public ResponseEntity<Void> save(@RequestBody ArticleRequest articleRequest) {
		ArticleSaveDTO articleSaveDTO = ArticleRequest.toSaveDTO(articleRequest);
		ArticleFindDTO articleFindDTO = articleService.save(articleSaveDTO);
		URI uri = getUri("board/{articleId}", articleFindDTO.getArticleId());
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<BoardResponse>>> findAll(Pageable pageable) {
		Page<ArticleFindDTO> articlePage = articleService.findAll(pageable);
		List<ArticleResponse> articleResponses = articlePage.getContent().stream()
			.map(ArticleResponse::toResponse)
			.collect(Collectors.toList());
		List<BoardResponse> boardResponses = articleResponses.stream()
			.map(articleResponse -> {
				List<CommentResponse> commentResponses = commentService.findAllByArticleId(
					articleResponse.getArticleId()).stream()
						.map(CommentResponse::toResponse)
						.collect(Collectors.toList());
				return new BoardResponse(articleResponse, commentResponses);
			})
			.collect(Collectors.toList());
		Page<BoardResponse> boardResponsePage = new PageImpl<BoardResponse>(
			boardResponses,
			articlePage.getPageable(),
			articlePage.getTotalElements()
		);

		PagedModel<EntityModel<BoardResponse>> pagedModel = pagedResourcesAssembler.toModel(boardResponsePage);

        return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/{articleId}")
	public ResponseEntity<BoardResponse> findById(@PathVariable("articleId") Long articleId, HttpServletRequest request,
		HttpServletResponse response) {
		
		String cookieName = "article_" + articleId + "_viewed";
		boolean isUniqueView = true;

		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals(cookieName)){
					isUniqueView = false;
					break;
				}
			}
		}

		if(isUniqueView){
			Cookie viewCookie = new Cookie(cookieName, "true");
			viewCookie.setMaxAge(300);
			viewCookie.setPath("/");
			response.addCookie(viewCookie);
			articleService.incrementViewCount(articleId);
		}

		ArticleResponse articleResponse = ArticleResponse.toResponse(articleService.findById(articleId));
		List<CommentResponse> commentResponses = commentService.findAllByArticleId(articleId).stream()
			.map(CommentResponse::toResponse)
			.collect(Collectors.toList());
		BoardResponse boardResponse = BoardResponse.builder()
			.articleResponse(articleResponse)
			.commentResponse(commentResponses)
			.build();

		return ResponseEntity.ok(boardResponse);
	}

	@PatchMapping("/{articleId}")
	public ResponseEntity<Void> update(@PathVariable("articleId") Long articleId, @RequestBody ArticleRequest articleRequest) {
		ArticleUpdateDTO articleUpdateDTO = ArticleRequest.toUpdateDTO(articleRequest);
		articleUpdateDTO.setArticleId(articleId);
		articleService.update(articleUpdateDTO);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{articleId}")
	public ResponseEntity<Void> deleteById(@PathVariable("articleId") Long articleId) {
		articleService.deleteById(articleId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{articleId}/restore")
	public ResponseEntity<Void> restoreById(@PathVariable("articleId") Long articleId) {
		articleService.restoreById(articleId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{articleId}/like")
	public ResponseEntity<Void> likeArticle(@PathVariable("articleId") Long articleId,
		@RequestHeader(value = "Member-Id", required = false) Long memberId,
		HttpServletRequest request) {

		if(memberId == null) memberId = generateGuestId(request);
		ArticleFindDTO articleFindDTO = articleService.findById(articleId);
		Article article = Article.builder()
			.articleId(articleFindDTO.getArticleId())
			.build();
		likeService.like(article, memberId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{articleId}/like")
	public ResponseEntity<Integer> getArticleLikes(@PathVariable("articleId") Long articleId) {
		ArticleFindDTO articleFindDTO = articleService.findById(articleId);
		Article article = Article.builder()
			.articleId(articleFindDTO.getArticleId())
			.build();
		int likeCount = likeService.getLikes(article);
		return ResponseEntity.ok(likeCount);
	}
}
