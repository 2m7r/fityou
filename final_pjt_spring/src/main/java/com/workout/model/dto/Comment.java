package com.workout.model.dto;

import java.time.LocalDateTime;

public class Comment {
	private long commentId; // 댓글 ID
    private long targetId; // 댓글이 속한 대상 ID (운동일기나 식단일기 ID)
    private String targetType; // 댓글이 속한 유형 ('diet' 또는 'workout')
    private long userId; // 댓글 작성자 ID
    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 댓글 작성일시
    
    
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getTargetId() {
		return targetId;
	}
	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
}
