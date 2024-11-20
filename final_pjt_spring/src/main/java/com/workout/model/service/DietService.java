package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.Diet;

public interface DietService {
	
	// 특정 유저의 식단일기 목록 조회
	List<Diet> getDietsByUserId(Long userId);
	
	// 특정 유저의 팔로잉 유저들의 식단일기 목록 조회
	List<Diet> getFollowingDietsByUserId(Long userId);
	
	// 식단일기 상세조회
	Diet getDietById(Long dietId);
	
	// 식단일기 등록
	int registDiet(Diet diet);
	
	// 식단일기 수정
	int modifyDiet(Diet diet);
	
	// 식단일기 삭제
	int removeDiet(Long dietId);
}
