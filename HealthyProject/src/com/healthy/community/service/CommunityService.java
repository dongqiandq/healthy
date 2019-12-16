package com.healthy.community.service;

import com.healthy.community.dao.CommunityDao;
import com.healthy.entity.Message;

public class CommunityService {
	private CommunityDao dao = new CommunityDao();
	
	public int addQuestion(Message message) {
		return dao.addQuestion(message);
	}
	

}
