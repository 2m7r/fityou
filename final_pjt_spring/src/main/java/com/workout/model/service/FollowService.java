package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.Follow;
import com.workout.model.dto.User;

public interface FollowService {
	
	int addFollow(Follow follow);
	
    int removeFollow(Follow follow);
    
    List<Long> getFollowingByUserId(Long followerId);
    
    List<Long> getFollowersByUserId(Long followingId);

	List<User> getFollowing(long userId);

	List<User> getFollower(long userId);

}
