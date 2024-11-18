package com.workout.model.dto;

import java.util.List;

public class RecordDto {
	private Long recordId;
    private Long userId;
    private String recordDate;
    private Diet diet;
    private Workout workout;
    private boolean isSharedWithTrainer;
    private int likeCount;
    private List<Long> likedUserIds; // 좋아요를 누른 사용자 ID 리스트
    private List<Comment> commentList; // 댓글 리스트
    
    
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
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public Diet getDiet() {
		return diet;
	}
	public void setDiet(Diet diet) {
		this.diet = diet;
	}
	public Workout getWorkout() {
		return workout;
	}
	public void setWorkout(Workout workout) {
		this.workout = workout;
	}
	public boolean isSharedWithTrainer() {
		return isSharedWithTrainer;
	}
	public void setSharedWithTrainer(boolean isSharedWithTrainer) {
		this.isSharedWithTrainer = isSharedWithTrainer;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public List<Long> getLikedUserIds() {
		return likedUserIds;
	}
	public void setLikedUserIds(List<Long> likedUserIds) {
		this.likedUserIds = likedUserIds;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
    
	
    
    
}
