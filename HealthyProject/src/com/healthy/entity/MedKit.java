package com.healthy.entity;

public class MedKit {
	private int id;
	private int userId;
	private int medicineChestId;
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
	public int getMedicineChestId() {
		return medicineChestId;
	}
	public void setMedicineChestId(int medicineChestId) {
		this.medicineChestId = medicineChestId;
	}
	@Override
	public String toString() {
		return "MedKit [id=" + id + ", userId=" + userId + ", medicineChestId=" + medicineChestId + "]";
	}
	public MedKit(int id, int userId, int medicineChestId) {
		super();
		this.id = id;
		this.userId = userId;
		this.medicineChestId = medicineChestId;
	}
	

}
