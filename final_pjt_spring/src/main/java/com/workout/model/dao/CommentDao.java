package com.workout.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workout.model.dto.Comment;

public interface CommentDao {
	// 댓글 등록
	int insertComment(Comment comment);

	// 댓글 삭제
	int deleteComment(long commentId);

	// 특정 대상(운동일기 또는 식단일기)에 달린 댓글 조회
	List<Comment> selectCommentsByTarget(@Param("targetId") long targetId, @Param("targetType") String targetType);

	// 댓글 수정
	int updateComment(Comment comment); // 댓글 수정
}