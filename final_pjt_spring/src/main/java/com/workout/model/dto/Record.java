package com.workout.model.dto;

public class Record {
	private Long recordId; // 기록 ID
    private Long userId; // 작성자 ID
    private String recordDate; // 기록 날짜 (YYYY-MM-DD)
    private String diet; // 식단 정보 (JSON 형식 또는 문자열)
    private String workout; // 운동 정보 (JSON 형식 또는 문자열)
    private String comments; // 다른 사용자의 코멘트
    private boolean isSharedOnlyTrainer; // 트레이너에게만 공유 여부
    
    
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
	public String getDiet() {
		return diet;
	}
	public void setDiet(String diet) {
		this.diet = diet;
	}
	public String getWorkout() {
		return workout;
	}
	public void setWorkout(String workout) {
		this.workout = workout;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public boolean isSharedOnlyTrainer() {
		return isSharedOnlyTrainer;
	}
	public void setSharedOnlyTrainer(boolean isSharedOnlyTrainer) {
		this.isSharedOnlyTrainer = isSharedOnlyTrainer;
	}
    
}
