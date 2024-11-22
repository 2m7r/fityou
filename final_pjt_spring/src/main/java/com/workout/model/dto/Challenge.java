package com.workout.model.dto;

public class Challenge {
	private Long challengeId;
    private String name;
    private Long creatorUserId; // 생성자 ID
    private String description;
    private int participantCount;
    private String durationStart; // 시작 날짜 (YYYY-MM-DD)
    private String durationEnd; // 종료 날짜 (YYYY-MM-DD)
    private String exerciseType;
    
    
	public Long getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getParticipantCount() {
		return participantCount;
	}
	public void setParticipantCount(int participantCount) {
		this.participantCount = participantCount;
	}
	public String getDurationStart() {
		return durationStart;
	}
	public void setDurationStart(String durationStart) {
		this.durationStart = durationStart;
	}
	public String getDurationEnd() {
		return durationEnd;
	}
	public void setDurationEnd(String durationEnd) {
		this.durationEnd = durationEnd;
	}
	public String getExerciseType() {
		return exerciseType;
	}
	public void setExerciseType(String exerciseType) {
		this.exerciseType = exerciseType;
	}
	
	
	@Override
	public String toString() {
		return "Challenge [challengeId=" + challengeId + ", name=" + name + ", creatorUserId=" + creatorUserId
				+ ", description=" + description + ", participantCount=" + participantCount + ", durationStart="
				+ durationStart + ", durationEnd=" + durationEnd + ", exerciseType=" + exerciseType + "]";
	}
	
	
    
}
