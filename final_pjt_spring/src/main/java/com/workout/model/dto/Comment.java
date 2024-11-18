package com.workout.model.dto;

public class Comment {
	private Long commentId; // 댓글 ID
    private Long recordId; // 댓글이 달린 기록 ID
    private Long userId; // 댓글 작성자 ID
    private String content; // 댓글 내용
    private String createdAt; // 댓글 작성 시간
    
    
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
