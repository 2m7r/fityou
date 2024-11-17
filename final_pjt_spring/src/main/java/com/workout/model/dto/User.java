package com.workout.model.dto;

public class User {
	private Long userId; // 고유 사용자 ID
    private String name; // 사용자 이름
    private int age; // 나이
    private String gender; // 성별 (M, F, O)
    private String email; // 이메일
    private String username; // 사용자 이름 (아이디)
    private String password; // 비밀번호
    private String role; // USER 또는 TRAINER
    private String gymName; // 헬스장 이름 (트레이너일 경우)
    private String awards; // 수상 경력 (트레이너일 경우)
    private String preferredExercise; // 선호 운동
    private boolean isPrivateAccount; // 공개/비공개 계정 여부
    
    
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getGymName() {
		return gymName;
	}
	public void setGymName(String gymName) {
		this.gymName = gymName;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getPreferredExercise() {
		return preferredExercise;
	}
	public void setPreferredExercise(String preferredExercise) {
		this.preferredExercise = preferredExercise;
	}
	public boolean isPrivateAccount() {
		return isPrivateAccount;
	}
	public void setPrivateAccount(boolean isPrivateAccount) {
		this.isPrivateAccount = isPrivateAccount;
	}
	
    
}
