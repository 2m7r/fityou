package com.workout.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.workout.model.dao.RecordDao;
import com.workout.model.dto.RecordDto;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Transactional
    public int registRecord(RecordDto recordDto, MultipartFile[] dietImages, MultipartFile workoutImage) {
        // 1. Diet 이미지 저장
        Long dietId = null;
        if (dietImages != null && dietImages.length > 0) {
            // diet 이미지 저장 로직
            String dietImagePath1 = saveImage(dietImages[0]);
            String dietImagePath2 = dietImages.length > 1 ? saveImage(dietImages[1]) : null;
            String dietImagePath3 = dietImages.length > 2 ? saveImage(dietImages[2]) : null;

            // Diet 레코드 생성
            DietDto dietDto = new DietDto();
            dietDto.setImage1(dietImagePath1);
            dietDto.setImage2(dietImagePath2);
            dietDto.setImage3(dietImagePath3);
            dietId = dietDao.insertDiet(dietDto); // Diet 저장 후 ID 반환
        }

        // 2. Workout 이미지 저장
        Long workoutId = null;
        if (workoutImage != null) {
            // workout 이미지 저장 로직
            String workoutImagePath = saveImage(workoutImage);

            // Workout 레코드 생성
            WorkoutDto workoutDto = new WorkoutDto();
            workoutDto.setImage(workoutImagePath);
            workoutId = workoutDao.insertWorkout(workoutDto); // Workout 저장 후 ID 반환
        }

        // 3. Record 저장
        recordDto.setDietId(dietId); // diet ID 설정
        recordDto.setWorkoutId(workoutId); // workout ID 설정
        int result = recordDao.insertRecord(recordDto); // Record 저장

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
