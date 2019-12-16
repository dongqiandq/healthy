package com.healthy.collection.service;

import java.util.List;

import com.healthy.collection.dao.CollectionDao;
import com.healthy.entity.CookBook;
import com.healthy.entity.KeepFit;
import com.healthy.entity.MedicineChest;

public class CollectionService {

	/**
	 * 健身等六块的收藏操作
	 */
	public int sixCollectionService(Integer operation,Integer userId,String tableName,Integer tableId){
		return new CollectionDao().sixCollectionDao(operation,userId, tableName, tableId);
	}
	
	/**
	 * 菜谱的收藏操作
	 */
	public int cookBookCollectionDao(Integer userId,Integer cookBookId){
		return new CollectionDao().cookBookCollectionDao(userId, cookBookId);
	}
	
	/**
	 * 查询收藏中的数据并发送到客户端
	 */
	public List<KeepFit> selectSixCollService(Integer userId){
		return new CollectionDao().selectSixCollDao(userId);
	}
	
	/**
	 * 查询食谱中的数据并发送到客户端
	 */
	public List<CookBook> selectCookBookCollService(Integer userId){
		return new CollectionDao().selectCookBookCollDao(userId);
	}
	
	/**
	 * 查询药箱中的数据并发送到客户端
	 */
	public List<MedicineChest> selectMedicineChestCollService(Integer userId){
		return new CollectionDao().selectMedicineChestCollDao(userId);
	}
}
