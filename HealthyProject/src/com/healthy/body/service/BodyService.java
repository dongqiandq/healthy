package com.healthy.body.service;

import java.util.List;

import com.healthy.body.dao.BodyDao;
import com.healthy.entity.HeartRecord;

public class BodyService {
	private BodyDao dao = new BodyDao();
	
	//根据用户ID找出对应的bodyId,根据userTel修改对应的数据
	public void fixBodyData(double height,double weight,double bim,String userTel) {
		dao.fixBodyDataByBodyUserTel(height, weight, bim, userTel);
	}
	
	//根据用户id找出该用户的所有心率记录
	public List<HeartRecord> findHeartRecordByUserId(String userTel){
		return dao.findHeartDataByUserTel(userTel);
	}
	
	//保存测量的心率值
	public boolean saveHeartRecord(HeartRecord record) {
		int i = dao.insertHeartRecord(record);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}
	
	//用户删除心率记录
	public void deleteRecord(int id) {
		dao.deleteRecord(id);
	}

}
