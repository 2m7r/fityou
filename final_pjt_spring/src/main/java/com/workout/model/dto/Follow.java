package com.workout.model.dto;

public class Follow {
	private Long followerId; // 팔로우를 하는 사용자
    private Long followingId; // 팔로우를 받는 사용자/트레이너 ID
    
    public Follow() {}
    
    public Follow(Long followerId, Long followingId) {
    	this.followerId = followerId;
    	this.followingId = followingId;
    }

	public Long getFollowerId() {
		return followerId;
	}

	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}

	public Long getFollowingId() {
		return followingId;
	}

	public void setFollowingId(Long followingId) {
		this.followingId = followingId;
	}

	@Override
	public String toString() {
		return "Follow [followerId=" + followerId + ", followingId=" + followingId + "]";
	}

}
