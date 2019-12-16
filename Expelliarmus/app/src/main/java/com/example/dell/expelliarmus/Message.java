package com.example.dell.expelliarmus;

import java.util.List;

public class Message {
	private List<Comment> comments;

	@Override
	public String toString() {
		return "Message{" +
				"comments=" + comments +
				", id=" + id +
				", userId=" + userId +
				", content='" + content + '\'' +
				", images='" + images + '\'' +
				'}';
	}

	public Message(List<Comment> comments, int id, int userId, String content, String images) {
		this.comments = comments;
		this.id = id;
		this.userId = userId;
		this.content = content;
		this.images = images;
	}

	public List<Comment> getComments() {

		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

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

	public Message( int userId, String content, String images) {
		super();
		this.userId = userId;
		this.content = content;
		this.images = images;
	}
	

}
