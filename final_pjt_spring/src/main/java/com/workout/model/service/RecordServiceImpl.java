package com.workout.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.model.dao.DietDao;
import com.workout.model.dao.RecordDao;
import com.workout.model.dao.WorkoutDao;
import com.workout.model.dto.RecordDto;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	private RecordDao recordDao;

	@Autowired
	private DietDao dietDao;

	@Autowired
	private WorkoutDao workoutDao;

	// 일기 등록
	@Transactional
	public int registRecord(RecordDto record) {
		// Record 저장
		int result = recordDao.insertRecord(record);
		if (result > 0) {
			// Diet 및 Workout 저장
			dietDao.insertDiet(record.getDiet());
			workoutDao.insertWorkout(record.getWorkout());
		}
		return result;
	}

	// 특정 일기 조회
	public RecordDto getRecordById(Long recordId) {
		return recordDao.selectRecordById(recordId);
	}

	// 일기 수정
	public int modifyRecord(RecordDto record) {
		return recordDao.updateRecord(record);
	}

	// 일기 삭제
	public int removeRecord(Long recordId) {
		return recordDao.deleteRecord(recordId);
	}

	// 유저별 일기 조회
	public List<RecordDto> getRecordsByUserId(Long userId) {
		return recordDao.selectRecordsByUserId(userId);
	}

	@Override
	public List<List<RecordDto>> getFollowersRecordsByUserId(Long userId) {
		// 1. 팔로우한 사용자들의 ID 목록 조회
		List<Long> followingIds = recordDao.selectFollowingIdsByUserId(userId);

		// 2. 각 사용자들의 기록을 조회하여 리스트에 추가
		List<List<RecordDto>> followersRecords = new ArrayList<>();
		for (Long followingId : followingIds) {
			List<RecordDto> records = recordDao.selectRecordsByUserId(followingId);
			followersRecords.add(records);
		}

		return followersRecords;
	}

}
