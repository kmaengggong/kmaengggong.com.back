package com.kmaengggong.kmaengggong.board.interfaces;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kmaengggong.kmaengggong.board.application.ArticleService;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;
import com.kmaengggong.kmaengggong.common.interfaces.CommonController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController extends CommonController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ArticleRequest articleRequest, UriComponentsBuilder ucb) {
        ArticleSaveDTO articleSaveDTO = ArticleRequest.toSaveDTO(articleRequest);
        ArticleFindDTO articleFindDTO = articleService.save(articleSaveDTO);
        URI uri = ucb
            .path("board/{articleId}")
            .buildAndExpand(articleFindDTO.getArticleId())
            .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll(Pageable pageable) {
        Page<ArticleFindDTO> articlePage = articleService.findAll(pageable);
        List<ArticleResponse> articleResponses = articlePage.getContent().stream()
            .map(ArticleResponse::toResponse)
            .collect(Collectors.toList());
        Page<ArticleResponse> articleResponsePage = new PageImpl<>(
            articleResponses, pageable, articlePage.getTotalElements());
        return ResponseEntity.ok(articleResponsePage.getContent());
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable("articleId") Long articleId) {
        ArticleFindDTO articleFindDTO = articleService.findById(articleId);
        ArticleResponse articleResponse = ArticleResponse.toResponse(articleFindDTO);
        return ResponseEntity.ok(articleResponse);
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
}
