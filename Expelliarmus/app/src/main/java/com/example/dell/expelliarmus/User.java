package com.example.dell.expelliarmus;

public class User {
	private int id;
	private String userName;
	private String userPassword;
	private int bodyId;
	private String userImage;
	private String sex;
	private String phoneNumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getBodyId() {
		return bodyId;
	}
	public void setBodyId(int bodyId) {
		this.bodyId = bodyId;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", userPassword=" + userPassword + ", bodyId=" + bodyId
				+ ", userImage=" + userImage + ", sex=" + sex + ", phoneNumber=" + phoneNumber + "]";
	}

	public User(){

	}

	public User(String userName,String phoneNumber,String sex,String url){
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.userImage = url;
	}

	public User(int id, String userName, String userPassword, int bodyId, String userImage, String sex,
                String phoneNumber) {
		super();
		this.id = id;
		this.userName = userName;
		this.userPassword = userPassword;
		this.bodyId = bodyId;
		this.userImage = userImage;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
	}
	

}
