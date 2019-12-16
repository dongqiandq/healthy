package com.healthy.entity;

import java.sql.Timestamp;

public class HeartRecord {
	private int id;
	private String userTel;
	private Timestamp time;
	private int userHeart;
	private int felling;
	private int huserBloodPressure;
	private int duserBloodPressure;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getUserHeart() {
		return userHeart;
	}
	public void setUserHeart(int userHeart) {
		this.userHeart = userHeart;
	}
	public int getFelling() {
		return felling;
	}
	public void setFelling(int felling) {
		this.felling = felling;
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

	@Override
	public String toString() {
		return "HeartRecord{" +
				"id=" + id +
				", userTel='" + userTel + '\'' +
				", time=" + time +
				", userHeart=" + userHeart +
				", felling=" + felling +
				", huserBloodPressure=" + huserBloodPressure +
				", duserBloodPressure=" + duserBloodPressure +
				'}';
	}

	public HeartRecord(int id, String userTel, Timestamp time, int userHeart, int felling, int huserBloodPressure, int duserBloodPressure) {
		this.id = id;
		this.userTel = userTel;
		this.time = time;
		this.userHeart = userHeart;
		this.felling = felling;
		this.huserBloodPressure = huserBloodPressure;
		this.duserBloodPressure = duserBloodPressure;
	}


	public HeartRecord(String userTel, int userHeart, int felling) {
		super();
		this.userTel = userTel;
		this.userHeart = userHeart;
		this.felling = felling;
	}
	

}
