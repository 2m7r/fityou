package com.workout.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workout.model.dto.Comment;
import com.workout.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-comment")
@Tag(name = "댓글 관리 API", description = "댓글 CRUD 기능을 제공합니다.")
public class CommentRestController {

	private final CommentService commentService;

	public CommentRestController(CommentService commentService) {
		this.commentService = commentService;
	}

	// 댓글 등록
	@PostMapping("/create")
	@Operation(summary = "댓글 등록", description = "식단일기나 운동일기에 댓글을 등록합니다.")
	public ResponseEntity<?> registerComment(@RequestBody Comment comment) {
		int result = commentService.addComment(comment);
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("댓글 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 등록 실패");
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
	public ResponseEntity<?> deleteComment(@PathVariable long commentId) {
		int result = commentService.removeComment(commentId);
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 삭제 실패");
	}

	// 특정 대상(식단/운동일기)에 달린 댓글 조회
	@GetMapping("/comment")
	@Operation(summary = "댓글 조회", description = "특정 대상(식단일기 또는 운동일기)에 달린 댓글을 조회합니다.")
	public ResponseEntity<List<Comment>> getCommentsByTarget(@RequestParam long targetId,
			@RequestParam String targetType) {
		List<Comment> comments = commentService.getCommentsByTarget(targetId, targetType);
		return ResponseEntity.ok(comments);
	}

	// 댓글 수정
	@PutMapping("/{commentId}")
	@Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
	public ResponseEntity<?> updateComment(@PathVariable long commentId, @RequestBody Comment comment) {
		comment.setCommentId(commentId); // commentId를 요청한 URL에서 가져와서 설정
		int result = commentService.updateComment(comment);
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 수정 실패");
	}

}
