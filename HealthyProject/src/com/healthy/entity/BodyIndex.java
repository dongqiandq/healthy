package com.healthy.entity;

public class BodyIndex {
	private int id;
	private String userTel;
	private double userHeight;
	private double userWeight;
	private int userHeart;
	private int huserBloodPressure;
	private int duserBloodPressure;
	private double userBIM;
	
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public BodyIndex(String userTel, double userHeight, double userWeight, double userBIM) {
		super();
		this.userTel = userTel;
		this.userHeight = userHeight;
		this.userWeight = userWeight;
		this.userBIM = userBIM;
	}
	public BodyIndex(int id, double userHeight, double userWeight, int userHeart, int huserBloodPressure,
			int duserBloodPressure, double userBIM) {
		super();
		this.id = id;
		this.userHeight = userHeight;
		this.userWeight = userWeight;
		this.userHeart = userHeart;
		this.huserBloodPressure = huserBloodPressure;
		this.duserBloodPressure = duserBloodPressure;
		this.userBIM = userBIM;
	}
	@Override
	public String toString() {
		return "BodyIndex [id=" + id + ", userHeight=" + userHeight + ", userWeight=" + userWeight + ", userHeart="
				+ userHeart + ", huserBloodPressure=" + huserBloodPressure + ", duserBloodPressure="
				+ duserBloodPressure + ", userBIM=" + userBIM + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(double userHeight) {
		this.userHeight = userHeight;
	}
	public double getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(double userWeight) {
		this.userWeight = userWeight;
	}
	public int getUserHeart() {
		return userHeart;
	}
	public void setUserHeart(int userHeart) {
		this.userHeart = userHeart;
	}
	public int getHuserBloodPressure() {
		return huserBloodPressure;
	}
	public void setHuserBloodPressure(int huserBloodPressure) {
		this.huserBloodPressure = huserBloodPressure;
	}
	public int getDuserBloodPressure() {
		return duserBloodPressure;
	}
	public void setDuserBloodPressure(int duserBloodPressure) {
		this.duserBloodPressure = duserBloodPressure;
	}
	public double getUserBIM() {
		return userBIM;
	}
	public void setUserBIM(double userBIM) {
		this.userBIM = userBIM;
	}
	
	

}
