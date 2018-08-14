package org.leo.jsm.api.service;

import org.leo.jsm.api.dao.RedisCacheDao;
import org.leo.jsm.api.dao.RedisDao;
import org.leo.jsm.api.entity.ProfileEntity;
import org.leo.jsm.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.leo.jsm.api.dao.ProfileDao;
import org.leo.jsm.core.BaseService;

@Service
public class ProfileService extends BaseService {

	@Autowired
	private ProfileDao profileDao;

	@Autowired
	private RedisDao redisDao;

	@Autowired
	private RedisCacheDao redisCacheDao;

	// 得到我的个人资料页
	public ProfileEntity getProfileById(String userId) throws BusinessException {
		return this.profileDao.getProfileById(userId);
	}


	// 个人资料页-更新用户姓名
	public boolean updateUserName(String userId, String userName) throws BusinessException {
		profileDao.updateUserName(userId, userName);
		return true;
	}

	//get user id by token
	public String getUserIdByToken(String token) {
		String userId = this.redisDao.getHashMap("auth.token." + token);
		return userId;
	}
}
