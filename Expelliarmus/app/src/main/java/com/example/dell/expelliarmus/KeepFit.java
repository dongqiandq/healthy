package com.example.dell.expelliarmus;

public class KeepFit {
	private int id;
	private String keyWord;
	private String titles;
	private String content;
	private String images;
	private int likeNumber;

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public KeepFit() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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
	public int getLikeNumber() {
		return likeNumber;
	}
	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}
	@Override
	public String toString() {
		return "Disease [id=" + id + ", keyWord=" + keyWord + ", content=" + content + ", images=" + images
				+ ", likeNumber=" + likeNumber + "]";
	}
	public KeepFit(int id, String keyWord, String content, String images, int likeNumber) {
		super();
		this.id = id;
		this.keyWord = keyWord;
		this.content = content;
		this.images = images;
		this.likeNumber = likeNumber;
	}

}
