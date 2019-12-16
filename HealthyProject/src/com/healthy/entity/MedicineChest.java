package com.healthy.entity;

public class MedicineChest {
	
	private int id;
	private String keyWord;
	private String name;
	private String englishName;
	private String pinYin;
	private String dosage;
	private String characters;
	private String indications;
	private String note;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getCharacters() {
		return characters;
	}
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	public String getIndications() {
		return indications;
	}
	public void setIndications(String indications) {
		this.indications = indications;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "MedicineChest [id=" + id + ", keyWord=" + keyWord + ", name=" + name + ", englishName=" + englishName
				+ ", pinYin=" + pinYin + ", dosage=" + dosage + ", characters=" + characters + ", indications="
				+ indications + ", note=" + note + "]";
	}
	public MedicineChest(int id, String keyWord, String name, String englishName, String pinYin, String dosage,
			String characters, String indications, String note) {
		super();
		this.id = id;
		this.keyWord = keyWord;
		this.name = name;
		this.englishName = englishName;
		this.pinYin = pinYin;
		this.dosage = dosage;
		this.characters = characters;
		this.indications = indications;
		this.note = note;
	}
	

}
