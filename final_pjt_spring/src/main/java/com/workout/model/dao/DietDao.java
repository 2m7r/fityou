package com.workout.model.dao;

import java.util.List;

import com.workout.model.dto.Diet;

public interface DietDao {
	
	// 특정 유저의 식단일기 목록 조회
	List<Diet> selectDietsByUserId(Long userId);
	
	// 특정 유저의 팔로잉 유저들의 식단일기 목록 조회
	List<Diet> selectFollowingDietsByUserId(Long userId);
	
	// 식단일기 상세조회
	Diet selectDietById(Long dietId);
	
	// 식단일기 등록
	int insertDiet(Diet diet);
	
	// 식단일기 수정
	boolean updateDiet(Long dietId, Diet diet);
	
	// 식단일기 삭제
	int deleteDiet(Long dietId);
	
	// 날짜로 식단일기 조회
	Diet selectDietByDate(String date, long userId);

}
