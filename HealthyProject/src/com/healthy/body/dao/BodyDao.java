package com.healthy.body.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthy.entity.HeartRecord;
import com.healthy.util.DBUtil.DBUtil;

public class BodyDao {
	private DBUtil util = new DBUtil();
	
	public int findBodyIdByUserTel(String userTel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = -1;
		try {
			con = DBUtil.getConnection();
			String sql = "select id from body_index where userTel=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, userTel);
			rs = ps.executeQuery();
			while(rs.next()) {
				id = rs.getInt("id");
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return id;
		}finally {
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	public void fixBodyDataByBodyUserTel(double height,double weight,double bim,String userTel) {
		int id = findBodyIdByUserTel(userTel);
		if(id>0) {
			util.fix("update body_index set userHeight=?,userWeight=?,userBIM=? where userTel=?", height,
					weight,bim,userTel);
		}else {
			util.fix("insert into body_index(userTel,userHeight,userWeight,userBIM) "
					+ "values(?,?,?,?)", 
					userTel,height,weight,bim);
		}
		
	}
	
	public List<HeartRecord> findHeartDataByUserTel(String userTel){
		List<HeartRecord> list = new ArrayList<HeartRecord>() ;
		Connection connection = null;
		PreparedStatement pStatement=null;
		ResultSet resultSet = null;
		try {
			connection = util.getConnection();
			pStatement = connection.prepareStatement("select * from heart_record where userTel=?");
			pStatement.setString(1, userTel);
			resultSet = pStatement.executeQuery();
			while(resultSet.next()) {
				HeartRecord record = new HeartRecord(resultSet.getInt("id"),
						resultSet.getString("userTel"),resultSet.getTimestamp("time"),
						resultSet.getInt("userHeart"), resultSet.getInt("felling"),
						resultSet.getInt("huserBloodPressure"),
						resultSet.getInt("huserBloodPressure"));
				list.add(record);
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			util.closeConnection(connection, pStatement, resultSet);
		}
		return list;
	}
	
	public int insertHeartRecord(HeartRecord record) {
		String sql = "insert into heart_record(userTel,time,userHeart,felling)"
				+ "values(?,?,?,?)";
		return util.fix(sql, record.getUserTel(),record.getTime(),record.getUserHeart(),record.getFelling());
	}
	
	public void deleteRecord(int id) {
		String sql = "delete from heart_record where id=?";
		util.fix(sql, id);
	}

}
