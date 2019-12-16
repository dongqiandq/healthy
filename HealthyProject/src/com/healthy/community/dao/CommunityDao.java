package com.healthy.community.dao;

import com.healthy.entity.Message;
import com.healthy.util.DBUtil.DBUtil;

public class CommunityDao {

	private DBUtil dbUtil = new DBUtil();
	
	public int addQuestion(Message message) {
		int r = -1;
		String sql = "insert into message(userId,content,images)"
				+ "values(?,?,?)";
		r = dbUtil.fix(sql, message.getUserId(),message.getContent(),message.getImages());
		return r;
	}
	
}
