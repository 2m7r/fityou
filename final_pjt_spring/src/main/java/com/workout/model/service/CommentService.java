package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.Comment;

public interface CommentService {
	int addComment(Comment comment);
    int removeComment(long commentId);
    List<Comment> getCommentsByTarget(long targetId, String targetType);
    int updateComment(Comment comment);  // 댓글 수정
}
