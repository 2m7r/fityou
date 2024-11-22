package com.workout.model.dao;

import java.util.List;

import com.workout.model.dto.Challenge;
import com.workout.model.dto.User;

public interface ChallengeDao {

	int insertChallenge(Challenge challenge);

	Challenge selectChallengeById(Long challengeId);

	int modifyChallenge(Challenge challenge);

	int deleteChallenge(Long challengeId);

	int countUp(Long challengeId);

	int intoChallenge(Long userId, Long challengeId);
	
	List<User> selectChallengeUsers(Long challengeId);

	List<Challenge> selectAllChallenges();

	List<Challenge> selectChallenges(long userId);
}
