package org.leo.jsm.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	public void addHashMap(String key, String value) {
		ValueOperations<String,String> vo = redisTemplate.opsForValue();
		vo.set(key, value);
	}

	public String getHashMap(String key) {
		ValueOperations<String,String> vo = redisTemplate.opsForValue();
		return vo.get(key);
	}

	public void deleteHashMap(String key) {
		redisTemplate.delete(key);
	}


}
