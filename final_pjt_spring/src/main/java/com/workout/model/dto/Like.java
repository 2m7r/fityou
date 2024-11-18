package com.workout.model.dto;

public class Like {
	private Long likeId; // 고유 좋아요 ID
    private Long recordId; // 좋아요가 눌린 기록 ID
    private Long userId; // 좋아요를 누른 사용자 ID
    private String createdAt; // 좋아요가 눌린 시간
    
    
	public Long getLikeId() {
		return likeId;
	}
	public void setLikeId(Long likeId) {
		this.likeId = likeId;
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
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
    
}
