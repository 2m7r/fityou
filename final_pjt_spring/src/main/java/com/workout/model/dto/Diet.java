package com.workout.model.dto;

public class Diet {
	private Long dietId;
	private Long userId;
	private String name;
	private String breakfastImagePath;
	private String lunchImagePath;
	private String dinnerImagePath;
	private String content;
	private String recordDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDietId() {
		return dietId;
	}
	public void setDietId(Long dietId) {
		this.dietId = dietId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getBreakfastImagePath() {
		return breakfastImagePath;
	}
	public void setBreakfastImagePath(String breakfastImagePath) {
		this.breakfastImagePath = breakfastImagePath;
	}
	public String getLunchImagePath() {
		return lunchImagePath;
	}
	public void setLunchImagePath(String lunchImagePath) {
		this.lunchImagePath = lunchImagePath;
	}
	public String getDinnerImagePath() {
		return dinnerImagePath;
	}
	public void setDinnerImagePath(String dinnerImagePath) {
		this.dinnerImagePath = dinnerImagePath;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	@Override
	public String toString() {
		return "Diet [dietId=" + dietId + ", userId=" + userId + ", name=" + name + ", breakfastImagePath="
				+ breakfastImagePath + ", lunchImagePath=" + lunchImagePath + ", dinnerImagePath=" + dinnerImagePath
				+ ", content=" + content + ", recordDate=" + recordDate + "]";
	}
	
}
