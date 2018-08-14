package org.leo.jsm.api.dao;

import org.leo.jsm.api.entity.ProfileEntity;
import org.leo.jsm.api.mapper.readonly.ProfileReadonlyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.leo.jsm.api.mapper.writable.ProfileMapper;

@Repository
public class ProfileDao {

	@Autowired
	private ProfileMapper profileMapper;
	@Autowired
	private ProfileReadonlyMapper profileReadonlyMapper;
    
	// 得到我的个人资料
	public ProfileEntity getProfileById(String userId) {
		return this.profileReadonlyMapper.getProfileById(userId);
	}

	// 个人资料页-更新用户姓名
	public void updateUserName(String userId, String userName) {
		this.profileMapper.updateUserName(userId, userName);
	}
}
