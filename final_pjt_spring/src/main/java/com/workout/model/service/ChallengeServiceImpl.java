package com.workout.model.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.workout.model.dao.ChallengeDao;
import com.workout.model.dto.Challenge;

public class ChallengeServiceImpl implements ChallengeService{
	
	@Autowired
	private ChallengeDao dao;

	@Override
	public int registChallenge(Challenge challenge) {
		return dao.insertChallenge(challenge);
	}

	@Override
	public Challenge getChallengeById(Long challengeId) {
		return dao.selectChallengeById(challengeId);
	}

	@Override
	public int modifyChallenge(Challenge challenge) {
		return dao.modifyChallenge(challenge);
	}

	@Override
	public int removeChallenge(Long challengeId) {
		return dao.deleteChallenge(challengeId);
	}

	@Override
	public int intoChallenge(Long challengeId, Long userId) {
		return dao.intoChallenge(challengeId, userId);
	}

}
