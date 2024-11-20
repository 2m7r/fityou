package com.workout.model.dao;

import com.workout.model.dto.Challenge;

public interface ChallengeDao {

	int insertChallenge(Challenge challenge);

	Challenge selectChallengeById(Long challengeId);

	int modifyChallenge(Challenge challenge);

	int deleteChallenge(Long challengeId);

	int intoChallenge(Long challengeId, Long userId);

}
