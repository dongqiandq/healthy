package com.healthy.entity;

public class Collection {
	private int id;
	private int userId;
	private String tableName;
	private int tableId;
	
	public Collection() {
		super();
	}
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
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	@Override
	public String toString() {
		return "Collection [id=" + id + ", userId=" + userId + ", tableName=" + tableName + ", tableId=" + tableId
				+ "]";
	}
	public Collection(int id, int userId, String tableName, int tableId) {
		super();
		this.id = id;
		this.userId = userId;
		this.tableName = tableName;
		this.tableId = tableId;
	}
	

}
