package com.workout.model.dao;

import java.util.List;

import com.workout.model.dto.RecordDto;

public interface RecordDao {

	int insertRecord(RecordDto record);

	RecordDto selectRecordById(Long recordId);

	int updateRecord(RecordDto record);

	int deleteRecord(Long recordId);

	List<RecordDto> selectRecordsByUserId(Long userId);

	List<Long> selectFollowingIdsByUserId(Long userId);

}
