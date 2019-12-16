package com.healthy.entity;

public class Message {
	private int id;
	private int userId;
	private String content;
	private String images;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", content=" + content + ", images=" + images + "]";
	}
	public Message( int userId, String content, String images) {
		this.userId = userId;
		this.content = content;
		this.images = images;
	}
	

}
