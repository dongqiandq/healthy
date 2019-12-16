package com.healthy.entity;

public class Menu {
	private int id;
	private int userId;
	private int cookBookId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCookBookId() {
		return cookBookId;
	}
	public void setCookBookId(int cookBookId) {
		this.cookBookId = cookBookId;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", userId=" + userId + ", cookBookId=" + cookBookId + "]";
	}
	public Menu(int id, int userId, int cookBookId) {
		super();
		this.id = id;
		this.userId = userId;
		this.cookBookId = cookBookId;
	}
	

}
