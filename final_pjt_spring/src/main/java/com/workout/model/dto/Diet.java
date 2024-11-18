package com.workout.model.dto;

public class Diet {
	private String breakfastText;
	private String breakfastImage;
	private String lunchText;
	private String lunchImage;
	private String dinnerText;
	private String dinnerImage;

	// 생성자, getters, setters
	public Diet(String breakfastText, String breakfastImage, String lunchText, String lunchImage, String dinnerText,
			String dinnerImage) {
		this.breakfastText = breakfastText;
		this.breakfastImage = breakfastImage;
		this.lunchText = lunchText;
		this.lunchImage = lunchImage;
		this.dinnerText = dinnerText;
		this.dinnerImage = dinnerImage;
	}

	public String getBreakfastText() {
		return breakfastText;
	}

	public void setBreakfastText(String breakfastText) {
		this.breakfastText = breakfastText;
	}

	public String getBreakfastImage() {
		return breakfastImage;
	}

	public void setBreakfastImage(String breakfastImage) {
		this.breakfastImage = breakfastImage;
	}

	public String getLunchText() {
		return lunchText;
	}

	public void setLunchText(String lunchText) {
		this.lunchText = lunchText;
	}

	public String getLunchImage() {
		return lunchImage;
	}

	public void setLunchImage(String lunchImage) {
		this.lunchImage = lunchImage;
	}

	public String getDinnerText() {
		return dinnerText;
	}

	public void setDinnerText(String dinnerText) {
		this.dinnerText = dinnerText;
	}

	public String getDinnerImage() {
		return dinnerImage;
	}

	public void setDinnerImage(String dinnerImage) {
		this.dinnerImage = dinnerImage;
	}
	
	
}
