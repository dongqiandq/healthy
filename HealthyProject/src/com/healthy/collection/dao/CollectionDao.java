package com.healthy.collection.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.healthy.entity.Collection;
import com.healthy.entity.CookBook;
import com.healthy.entity.Disease;
import com.healthy.entity.KeepFit;
import com.healthy.entity.MedicineChest;
import com.healthy.entity.Menu;
import com.healthy.util.DBUtil.DBUtil;

public class CollectionDao {

	/**
	 * 健身等六块的收藏操作
	 */
	public int sixCollectionDao(Integer operation,Integer userId,String tableName,Integer tableId){
		DBUtil dbUtil=new DBUtil();
		String sql="";
		if(operation==0){
			sql="delete from collection where userId=? and tableName=? and tableId=?";
		}if(operation==1){
			sql="insert into collection(userId,tableName,tableId) values(?,?,?)";			
		}
		return dbUtil.fix(sql, userId,tableName,tableId);
	}
	
	public void coll(String tName,Integer userId,String tableName,Integer tableId){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		con=DBUtil.getConnection();
		try {
			String name=tName;
			String sql="insert into"+" "+name+"(userId,tableName,tableId) values(?,?,?)";
			ps=con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, tableName);
			ps.setInt(3, tableId);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 菜谱的收藏操作
	 */
	public int cookBookCollectionDao(Integer userId,Integer cookBookId){
		DBUtil dbUtil=new DBUtil();
		String sql="insert into menu(userId,cookBookId) values(?,?)";
		return dbUtil.fix(sql, userId,cookBookId);
	}
	
	/**
	 * 查询收藏中的数据并发送到客户端
	 */
	public List<KeepFit> selectSixCollDao(Integer userId){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<Collection> collections=new ArrayList<Collection>();
		List<KeepFit> lists=new ArrayList<KeepFit>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select tableName,tableId from collection where userId=?");
			ps.setInt(1, userId);
			rs=ps.executeQuery();
			while(rs.next()){
				if(!"cookbook".equals(rs.getString(1)) && !"medicine_chest".equals(rs.getString(1))){
					Collection collection=new Collection();
					collection.setTableName(rs.getString(1));
					collection.setTableId(rs.getInt(2));
					collections.add(collection);
				}
			}
			
			for(Collection collection:collections){
				String sql="";
				switch(collection.getTableName()){
				case "keep_fit":
					sql="select * from keep_fit where id=?";
					break;
				case "disease":
					sql="select * from disease where id=?";
					break;
				case "health":
					sql="select * from health where id=?";
					break;
				case "eye_protection":
					sql="select * from eye_protection where id=?";
					break;
				case "psychological":
					sql="select * from psychological where id=?";
					break;
				case "health_care":
					sql="select * from health_care where id=?";
					break;
				}
				ps=con.prepareStatement(sql);
				ps.setInt(1, collection.getTableId());
				rs=ps.executeQuery();
				if(rs.next()){
					KeepFit keepFit=new KeepFit();
					keepFit.setId(rs.getInt(1));
					keepFit.setKeyWord(rs.getString(2));
					keepFit.setContent(rs.getString(3));
					keepFit.setImages(rs.getString(4));
					keepFit.setLikeNumber(rs.getInt(5));
					lists.add(keepFit);
				}
			}
			return lists;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 查询食谱中的数据并发送到客户端
	 */
	public List<CookBook> selectCookBookCollDao(Integer userId){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<CookBook> lists=new ArrayList<CookBook>();
		List<Integer> tableIds=new ArrayList<>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select cookBookId from menu where userId=?");
			ps.setInt(1, userId);
			rs=ps.executeQuery();
			while(rs.next()){
				tableIds.add(rs.getInt(1));				
			}
			for(Integer i:tableIds){
				ps=con.prepareStatement("select * from cookbook where id=?");
				ps.setInt(1, i);
				rs=ps.executeQuery();
				if(rs.next()){
					CookBook cookBook=new CookBook(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getInt(8)
							);
					lists.add(cookBook);
				}
			}
			return lists;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 查询药箱中的数据并发送到客户端
	 */
	public List<MedicineChest> selectMedicineChestCollDao(Integer userId){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<MedicineChest> lists=new ArrayList<MedicineChest>();
		List<Integer> tableIds=new ArrayList<>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select tableId from collection where userId=? and tableName='medicine_chest'");
			ps.setInt(1, userId);
			rs=ps.executeQuery();
			while(rs.next()){
				tableIds.add(rs.getInt(1));				
			}
			for(Integer i:tableIds){
				ps=con.prepareStatement("select * from medicine_chest where id=?");
				ps.setInt(1, i);
				rs=ps.executeQuery();
				if(rs.next()){
					MedicineChest medicineChest=new MedicineChest(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9)
							);
					lists.add(medicineChest);
				}
			}
			return lists;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
}
