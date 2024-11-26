package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workout.model.dao.FollowDao;
import com.workout.model.dto.Follow;
import com.workout.model.dto.User;

@Service
public class FollowServiceImpl implements FollowService{
	
	@Autowired
	private FollowDao followDao;

	@Override
	public int addFollow(Follow follow) {
		return followDao.insertFollow(follow);
	}

	@Override
	public int removeFollow(Follow follow) {
		return followDao.deleteFollow(follow);
	}

	@Override
	public List<Long> getFollowingByUserId(Long followerId) {
		return followDao.selectFollowingByUserId(followerId);
	}

	@Override
	public List<Long> getFollowersByUserId(Long followingId) {
		return followDao.selectFollowersByUserId(followingId);
	}

	@Override
	public List<User> getFollowing(long userId) {
		return followDao.selectFollowings(userId);
	}

	@Override
	public List<User> getFollower(long userId) {
		return followDao.selectFollowers(userId);
	}

}
