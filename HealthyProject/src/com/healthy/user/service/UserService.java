package com.healthy.user.service;

import com.healthy.entity.User;
import com.healthy.user.dao.UserDao;

public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * 
	     * @Title: fixUserPwd
	     * @Description: 修改用户名密码
	     * @param @param pwd 用户名的新密码
	     * @param @param id 要修改的用户名id
	     * @return void 返回类型
	     * @throws
	 */
	public void fixUserPwd(String pwd,int id) {
		userDao.fixUserPassword(pwd, id);
	}
	
	
	public int resetUserPwd(String pwd,String tel) {
		return userDao.resetUserPassword(pwd, tel);
	}
	
	public User findUserByTele(String tel) {
		return userDao.findUserByPhoneNumber(tel);
	}
	
	public User registerUser(String userPassword,String phoneNumber) {
		int id = userDao.registerUser( userPassword, phoneNumber);
		if(id!=0) {
			return new User(id,phoneNumber,userPassword,"/img/header.png", "男",phoneNumber);
		}else {
			return null;
		}
	}
	
	public int registerQQuser(User user) {
		return userDao.registerQQuser(user);
	}
	
	/**
	 * 功能：用户的普通登录
	 * 描述：根据用户的用户名和密码去数据库查询
	 */
	public String normalLoginService(String phone,String pwd){
		return new UserDao().normalLoginDao(phone, pwd);
	}
	
	/**
	 * 功能：修改用户昵称
	 * 描述：根据用户的id修改用户昵称
	 */
	public int updateUserNameService(Integer id,String userName){
		return new UserDao().updateUserNameDao(id, userName);
	}
	
	/**
	 * 功能：修改用户性别
	 * 描述：根据用户的id修改用户性别
	 */
	public int updateUserSexService(Integer id,String userSex){
		return new UserDao().updateUserSexDao(id, userSex);
	}
	
	/**
	 *功能：最近阅读
     *描述：点击过的文章加入到最近阅读中
	 */
	public int insertRecentReadService(Integer userId,String tableName,Integer tableId){
		return new UserDao().insertRecentReadDao(userId, tableName, tableId);
	}


}
