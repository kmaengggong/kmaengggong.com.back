package com.kmaengggong.kmaengggong.board.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.board.application.dto.ArticleFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.ArticleUpdateDTO;

@Service
public interface ArticleService {
    ArticleFindDTO save(ArticleSaveDTO articleSaveDTO);

    List<ArticleFindDTO> findAll();
    Page<ArticleFindDTO> findAll(Pageable pageable);
    ArticleFindDTO findById(Long articleId);

    void update(ArticleUpdateDTO articleUpdateDTO);

    void deleteById(Long articleId);
}