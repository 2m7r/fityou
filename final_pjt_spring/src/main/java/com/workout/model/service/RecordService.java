package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.RecordDto;

public interface RecordService {
	
	// 일기 등록
	int registRecord(RecordDto record);
	
	// 일기 상세 조회
	RecordDto getRecordById(Long recordId);
	
	// 일기 수정
	int modifyRecord(RecordDto record);
	
	// 일기 삭제
	int removeRecord(Long recordId);
	
	// 특정 유저의 일기 목록 조회
	List<RecordDto> getRecordsByUserId(Long userId);
	
	// 팔로잉 유저들의 일기 목록 조회
	List<List<RecordDto>> getFollowersRecordsByUserId(Long userId);
	
}
