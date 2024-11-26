package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workout.model.dao.DietDao;
import com.workout.model.dto.Diet;

@Service
public class DietServiceImpl implements DietService{

	@Autowired
	private DietDao dietDao;
	
	@Override
	public List<Diet> getDietsByUserId(Long userId) {
		return dietDao.selectDietsByUserId(userId);
	}
	
	@Override
	public List<Diet> getFollowingDietsByUserId(Long userId) {
		return dietDao.selectFollowingDietsByUserId(userId);
	}

	@Override
	public Diet getDietById(Long dietId) {
		return dietDao.selectDietById(dietId);
	}

	@Override
	public int registDiet(Diet diet) {
		return dietDao.insertDiet(diet);
	}

	@Override
	public boolean modifyDiet(Long dietId, Diet diet) {
		return dietDao.updateDiet(dietId, diet);
	}

	@Override
	public int removeDiet(Long dietId) {
		return dietDao.deleteDiet(dietId);
	}

	@Override
	public Diet getDietByDate(String date, long userId) {
		return dietDao.selectDietByDate(date, userId);
	}

}
