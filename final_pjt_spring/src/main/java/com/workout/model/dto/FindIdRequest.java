package com.workout.model.dto;

public class FindIdRequest {
	private String email;
    private String name;
    private String username;
    private String securityQuestion;
    private String securityAnswer;  // 본인 확인 질문에 대한 답변
    
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
	@Override
	public String toString() {
		return "FindIdRequest [email=" + email + ", name=" + name + ", username=" + username + ", securityQuestion="
				+ securityQuestion + ", securityAnswer=" + securityAnswer + "]";
	}
	
	
    
}
