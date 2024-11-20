package com.workout.model.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
	
	private final Set<String> blacklist = new HashSet<>();
	
	// 토큰 블랙리스트에 추가
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
