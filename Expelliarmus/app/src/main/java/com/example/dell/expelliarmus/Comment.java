package com.example.dell.expelliarmus;

public class Comment {
	
	private int id;
	private int messageId;
	private String content;
	private int sendPersonId;
	private int likeNumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSendPersonId() {
		return sendPersonId;
	}
	public void setSendPersonId(int sendPersonId) {
		this.sendPersonId = sendPersonId;
	}
	public int getLikeNumber() {
		return likeNumber;
	}
	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", messageId=" + messageId + ", content=" + content + ", sendPersonId="
				+ sendPersonId + ", likeNumber=" + likeNumber + "]";
	}
	public Comment(int id, int messageId, String content, int sendPersonId, int likeNumber) {
		super();
		this.id = id;
		this.messageId = messageId;
		this.content = content;
		this.sendPersonId = sendPersonId;
		this.likeNumber = likeNumber;
	}
	

}
