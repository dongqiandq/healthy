package com.healthy.type.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.healthy.entity.CookBook;
import com.healthy.entity.KeepFit;
import com.healthy.entity.MedicineChest;
import com.healthy.util.DBUtil.DBUtil;

public class TypeDao {
	/**
	 * 功能：文章点赞数目加一
	 * 描述：根据表名和表的某一元组id值使点赞数量加一
	 */
	public int updatePassageLikeNumDao(String tableName,Integer id,Integer status){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select likeNumber from "+tableName+" where id=?");
			ps.setInt(1, id);
			rs=ps.executeQuery();
			if(rs.next()){
				Integer likeNumber=rs.getInt(1);
				String sql="update "+tableName+" set likeNumber=? where id=?";
				DBUtil dbUtil=new DBUtil();
				likeNumber+=status;
				int row=dbUtil.fix(sql,likeNumber,id);
				return row;
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 功能：查询某张表的所有数据，并在客户端显示
	 * 描述：根据表名查询某张表
	 */
	public List<KeepFit> displayKeepFitDao(String tableName){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<KeepFit> keepFits=new ArrayList<KeepFit>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from "+tableName);
			rs=ps.executeQuery();
			while(rs.next()){
				KeepFit keepFit=new KeepFit(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6));
				keepFits.add(keepFit);
			}
			return keepFits;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 功能：查询某张表的部分数据，并在客户端显示
	 * 描述：根据表名和关键字查询某张表
	 */
	public List<KeepFit> displaySomeKeepFitDao(String tableName,String search){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<KeepFit> keepFits=new ArrayList<KeepFit>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from "+tableName+" "+"where KeyWord like ?");
			ps.setString(1, "%"+search+"%");
			rs=ps.executeQuery();
			while(rs.next()){
				KeepFit keepFit=new KeepFit(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6));
				keepFits.add(keepFit);
			}
			System.out.println(keepFits.size());
			return keepFits;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 功能：查询药箱的某类数据，并在客户端显示
	 * 描述：根据表名和药品类型查询数据库
	 */
	public List<MedicineChest> displayMedicineChestDao(String tableName,String category){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<MedicineChest> medicineChests=new ArrayList<MedicineChest>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from "+tableName+" "+"where KeyWord like ?");
			ps.setString(1, "%"+category+"%");
			rs=ps.executeQuery();
			while(rs.next()){
				MedicineChest medicineChest=new MedicineChest(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9));
				medicineChests.add(medicineChest);
			}
			return medicineChests;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 功能：查询菜谱
	 * 描述：查询菜谱中点赞量最高的前5个菜谱
	 */
	public List<CookBook> displayTop5Dao(){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<CookBook> cookBooks=new ArrayList<>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from cookbook order by likeNumber desc limit 0,5");
			rs=ps.executeQuery();
			while(rs.next()){
				CookBook cookBook=new CookBook(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getInt(8));
				cookBooks.add(cookBook);
				System.out.println("likeNumber"+rs.getInt(8));
			}
			return cookBooks;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 功能：查询某种类型的菜单
	 * 描述：根据关键字查询一个菜单
	 */
	public List<CookBook> displayOneTypeDao(String category){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<CookBook> cookBooks=new ArrayList<>();
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from cookbook where KeyWord like ?");
			ps.setString(1, "%"+category+"%");
			rs=ps.executeQuery();
			while(rs.next()){
				CookBook cookBook=new CookBook(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getInt(8));
				cookBooks.add(cookBook);
			}
			return cookBooks;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
}
