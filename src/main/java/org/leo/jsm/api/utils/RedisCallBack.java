package org.leo.jsm.api.utils;

import redis.clients.jedis.Jedis;
 
public interface RedisCallBack {
    public boolean exec(Jedis jedis,String key);
}
