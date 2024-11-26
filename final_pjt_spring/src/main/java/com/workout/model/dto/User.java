package com.workout.model.dto;

import java.util.ArrayList;
import java.util.List;

public class User {
	private Long userId; // 고유 사용자 ID
    private String name; // 사용자 이름
    private String birthDate; // 생년월일
    private String gender; // 성별 (M, F, O)
    private String email; // 이메일
    private String username; // 사용자 이름 (아이디)
    private String password; // 비밀번호
    private List<String> preferredExercises = new ArrayList<>(); // 빈 리스트로 초기화 // 선호 운동 리스트
    private boolean isPrivateAccount; // 공개/비공개 계정 여부
    private String profileImage;
    private String phoneNum;
    
    private String userDescription;
    
    // 본인 확인 질문과 답변
    private String securityQuestion;  // 본인 확인 질문
	private String securityAnswer;  // 본인 확인 답변
    
	
	
	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
    
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getPreferredExercises() {
		return preferredExercises;
	}
	public void setPreferredExercises(List<String> preferredExercises) {
		this.preferredExercises = preferredExercises;
	}
	public boolean isPrivateAccount() {
		return isPrivateAccount;
	}
	public void setPrivateAccount(boolean isPrivateAccount) {
		this.isPrivateAccount = isPrivateAccount;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", birthDate=" + birthDate + ", gender=" + gender
				+ ", email=" + email + ", username=" + username + ", password=" + password + ", preferredExercises="
				+ preferredExercises + ", isPrivateAccount=" + isPrivateAccount + ", profileImage=" + profileImage
				+ ", phoneNum=" + phoneNum + ", userDescription=" + userDescription + ", securityQuestion="
				+ securityQuestion + ", securityAnswer=" + securityAnswer + "]";
	}
	
}
