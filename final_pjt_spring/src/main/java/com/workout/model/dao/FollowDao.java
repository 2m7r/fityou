package com.workout.model.dao;

import java.util.List;

import com.workout.model.dto.Follow;

public interface FollowDao {

	// 팔로우 등록
	int insertFollow(Follow follow);

	// 팔로잉 삭제
	int deleteFollow(Follow follow);

	// 특정 사용자의 팔로잉 목록 조회
	List<Long> selectFollowingByUserId(Long followerId);

	// 특정 사용자의 팔로워 목록 조회
	List<Long> selectFollowersByUserId(Long followingId);

}
