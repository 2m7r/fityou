package com.workout.model.dao;

import com.workout.model.dto.Diet;

public interface DietDao {
	// Diet 등록
    int insertDiet(Diet diet);

    // Diet 수정
    int updateDiet(Diet diet);

    // Diet 삭제
    int deleteDiet(Long dietId);
}
