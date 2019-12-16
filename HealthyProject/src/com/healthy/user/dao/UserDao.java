package com.healthy.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.healthy.entity.User;
import com.healthy.util.DBUtil.DBUtil;

public class UserDao {
	private DBUtil dbUtil = new DBUtil();
	
	/**
	 * 
	     * @Title: fixUserPassword
	     * @Description: 根据id值修改用户密码
	     * @param @param pwd
	     * @param @param id 参数
	     * @return void 返回类型
	     * @throws
	 */
	public void fixUserPassword(String pwd,int id) {
		dbUtil.fix("update user set userPassword=? where id=?", pwd,id);
	}
	
	/**
	 * 
	     * @Title: resetUserPassword
	     * @Description: 用户忘记密码，重置密码
	     * @param @param pwd
	     * @param @param tel 参数
	     * @return void 返回类型
	     * @throws
	 */
	public int resetUserPassword(String pwd,String tel) {
		return dbUtil.fix("update user set userPassword=? where phoneNumber=?", pwd,tel);
	}
	
	/**
	 * 根据手机号查找用户是否存在，存在则登陆成功
	     * @Title: findUserByPhoneNumber
	     * @Description: TODO(这里用一句话描述这个方法的作用)
	     * @param @return 参数
	     * @return User 返回类型
	     * @throws
	 */
	public User findUserByPhoneNumber(String tel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object object = null;
		User user = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement("select * from user where phoneNumber=?");
			ps.setString(1, tel);
			rs = ps.executeQuery();
			while(rs.next()) {
				user = new User(rs.getInt("id"),rs.getString("userName") ,
						rs.getString("userPassword"), rs.getInt("bodyId"),
						rs.getString("userImage"), rs.getString("sex"), rs.getString("phoneNumber"));
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}finally {
			DBUtil.closeConnection(con, ps, rs);
		}

	}

	/**
	 * 
	     * @Title: registerUser
	     * @Description: 注册新用户，默认性别为男，头像使用默认，昵称默认使用手机号
	     * @param @param userName
	     * @param @param userPassword
	     * @param @param phoneNumber
	     * @param @return 参数
	     * @return int 返回类型
	     * @throws
	 */
	public int registerUser(String userPassword,String phoneNumber) {
		User user = findUserByPhoneNumber(phoneNumber);
		if(user==null) {
			return dbUtil.getInsertId("insert into user(userName,userPassword,userImage,sex,phoneNumber) "
					+ "values(?,?,?,?,?)",phoneNumber,userPassword,"/img/header.png","男",phoneNumber);
		}else{
			return 0;
		}
		
	}
	
	public int registerQQuser(User user) {
		int id = -1;
		if(user!=null) {
			id = dbUtil.getInsertId("insert into user(userName,sex,phoneNumber,userImage)"
					+ "values(?,?,?,?)", user.getUserName(),user.getSex(),user.getPhoneNumber(),user.getUserImage());
		}
		return id;
	}
	
	
	/**
	 * 功能：用户的普通登录
	 * 描述：根据用户的用户名和密码去数据库查询
	 */
	public String normalLoginDao(String phone,String pwd){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String login="no";
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select * from user where phoneNumber=? and userPassword=?");
			ps.setString(1, phone);
			ps.setString(2, pwd);
			rs=ps.executeQuery();
			if(rs.next()){
				login="ok";
			}
			return login;
		} catch (SQLException e) {
			e.printStackTrace();
			return login; 
		}finally{
			DBUtil.closeConnection(con, ps, rs);
		}
	}
	
	/**
	 * 功能：修改用户昵称
	 * 描述：根据用户的id修改用户昵称
	 */
	public int updateUserNameDao(Integer id,String userName){
		DBUtil dbUtil=new DBUtil();
		String sql="update user set userName=? where id=?";
		int row=dbUtil.fix(sql, userName,id);
		return row;
	}
	
	/**
	 * 功能：修改用户性别
	 * 描述：根据用户的id修改用户性别
	 */
	public int updateUserSexDao(Integer id,String userSex){
		DBUtil dbUtil=new DBUtil();
		String sql="update user set sex=? where id=?";
		int row=dbUtil.fix(sql, userSex,id);
		return row;
	}
	
	/**
	 *功能：最近阅读
     *描述：点击过的文章加入到最近阅读中
	 */
	public int insertRecentReadDao(Integer userId,String tableName,Integer tableId){
		DBUtil dbUtil=new DBUtil();
		String sql="insert into scan_history(userId,tableName,tableId) values(?,?,?)";
		int row=dbUtil.fix(sql, userId,tableName,tableId);
		return row;
	}

	
	
}
