package com.kmaengggong.kmaengggong.board.interfaces;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kmaengggong.kmaengggong.board.application.CommentService;
import com.kmaengggong.kmaengggong.board.application.LikeService;
import com.kmaengggong.kmaengggong.board.application.dto.CommentFindDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentSaveDTO;
import com.kmaengggong.kmaengggong.board.application.dto.CommentUpdateDTO;
import com.kmaengggong.kmaengggong.board.domain.Comment;
import com.kmaengggong.kmaengggong.board.interfaces.dto.CommentRequest;
import com.kmaengggong.kmaengggong.board.interfaces.dto.CommentResponse;
import com.kmaengggong.kmaengggong.common.interfaces.CommonController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController extends CommonController {
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CommentRequest commentRequest) {
        CommentSaveDTO commentSaveDTO = CommentRequest.toSaveDTO(commentRequest);
        commentService.save(commentSaveDTO);
        URI uri = getUri("board/{articleId}", commentRequest.getArticleId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAll(@RequestParam(value = "articleId", required = false) Long articleId) {
        List<CommentFindDTO> commentDtos;

        if(articleId == null) commentDtos = commentService.findAll();
        else commentDtos = commentService.findAllByArticleId(articleId);

        List<CommentResponse> commentResponses = commentDtos.stream()
            .map(CommentResponse::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findById(@PathVariable("commentId") Long commentId) {
        CommentResponse commentResponse = CommentResponse.toResponse(commentService.findById(commentId));
        return ResponseEntity.ok(commentResponse);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> update(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentUpdateDTO commentUpdateDTO = CommentRequest.toUpdateDTO(commentRequest);
        commentUpdateDTO.setCommentId(commentId);
        commentService.update(commentUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable("commentId") Long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable("commentId") Long commentId,
        @RequestHeader(value = "Member-Id", required = false) Long memberId,
        HttpServletRequest request) {
        System.out.println(memberId);
        if(memberId == null) memberId = generateGuestId(request);
        CommentFindDTO commentFindDTO = commentService.findById(commentId);
        Comment comment = Comment.builder()
            .commentId(commentFindDTO.getCommentId())
            .build();
        likeService.like(comment, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<Integer> getCommentLikes(@PathVariable("commentId") Long commentId) {
        CommentFindDTO commentFindDTO = commentService.findById(commentId);
        Comment comment = Comment.builder()
            .commentId(commentFindDTO.getCommentId())
            .build();
        int likeCount = likeService.getLikes(comment);
        return ResponseEntity.ok(likeCount);
    }
}
