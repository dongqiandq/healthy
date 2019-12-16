package com.healthy.type.service;

import java.util.List;

import com.healthy.entity.CookBook;
import com.healthy.entity.KeepFit;
import com.healthy.entity.MedicineChest;
import com.healthy.type.dao.TypeDao;

public class TypeService {
	/**
	 * 功能：文章点赞数目加一
	 * 描述：根据表名和表的某一元组id值使点赞数量加一
	 */
	public int updatePassageLikeNumService(String tableName,Integer id,Integer status){
		return new TypeDao().updatePassageLikeNumDao(tableName, id,status);
	}
	
	/**
	 * 功能：查询某张表的所有数据，并在客户端显示
	 * 描述：根据表名查询某张表
	 */
	public List<KeepFit> displayKeepFitService(String tableName){
		return new TypeDao().displayKeepFitDao(tableName);
	}
	
	/**
	 * 功能：查询某张表的部分数据，并在客户端显示
	 * 描述：根据表名和关键字查询某张表
	 */
	public List<KeepFit> displaySomeKeepFitService(String tableName,String search){
		return new TypeDao().displaySomeKeepFitDao(tableName, search);
	}
	
	/**
	 * 功能：查询药箱的某类数据，并在客户端显示
	 * 描述：根据表名和药品类型查询数据库
	 */
	public List<MedicineChest> displayMedicineChestService(String tableName,String category){
		return new TypeDao().displayMedicineChestDao(tableName, category);
	}
	
	/**
	 * 功能：查询菜谱
	 * 描述：查询菜谱中点赞量最高的前5个菜谱
	 */
	public List<CookBook> displayTop5Service(){
		return new TypeDao().displayTop5Dao();
	}
	
	/**
	 * 功能：查询某种类型的菜单
	 * 描述：根据关键字查询一个菜单
	 */
	public List<CookBook> displayOneTypeService(String category){
		return new TypeDao().displayOneTypeDao(category);
	}
}
