package com.workout.model.dto;

public class Follow {
	private Long userId;
    private Long followingId; // 팔로우하는 사용자/트레이너 ID
    
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getFollowingId() {
		return followingId;
	}
	public void setFollowingId(Long followingId) {
		this.followingId = followingId;
	}

}
