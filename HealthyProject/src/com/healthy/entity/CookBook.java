package com.healthy.entity;

public class CookBook {
	private int id;
	private String keyWord;
	private String description;
	private String images;
	private String name;
	private String material;
	private String steps;
	private int likeNumber;
	
	public CookBook() {
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public int getLikeNumber() {
		return likeNumber;
	}
	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}
	@Override
	public String toString() {
		return "CookBook [id=" + id + ", keyWord=" + keyWord + ", description=" + description + ", images=" + images
				+ ", name=" + name + ", material=" + material + ", steps=" + steps + ", likeNumber=" + likeNumber + "]";
	}
	public CookBook(int id, String keyWord, String description, String images, String name, String material,
			String steps, int likeNumber) {
		super();
		this.id = id;
		this.keyWord = keyWord;
		this.description = description;
		this.images = images;
		this.name = name;
		this.material = material;
		this.steps = steps;
		this.likeNumber = likeNumber;
	}
	

}
