package org.leo.jsm.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * 操作Redis类
 */
public class RedisUtils {

    /**
     * 日志处理
     */
    private final static Logger logger = Logger.getLogger(RedisUtils.class);

    /**
     * Redis服务器IP
     */
    private final static String ADDR_ARRAY = PropertyUtils.getRedis("redis.server");

    /**
     * Redis的MASTER端口号
     */
    private final static int PORT_MASTER = Integer.parseInt(PropertyUtils.getRedis("redis.port"));

    /** Redis的SLAVE端口号 */
    //private final static int PORT_SLAVE = Integer.parseInt(PropertyUtils.getRedis("redis.port").split(",")[1]);

    /**
     * 访问密码
     */
    private final static String AUTH = PropertyUtils.getRedis("redis.auth");

    /**
     * 可用连接实例的最大数目，默认值为8；
     * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
     */
    private final static int MAX_ACTIVE = Integer.parseInt(PropertyUtils.getRedis("redis.max_active"));

    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
     */
    private final static int MAX_IDLE = Integer.parseInt(PropertyUtils.getRedis("redis.max_idle"));

    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
     */
    private final static int MAX_WAIT = Integer.parseInt(PropertyUtils.getRedis("redis.max_wait"));

    /**
     * 超时时间
     */
    private final static int TIMEOUT = Integer.parseInt(PropertyUtils.getRedis("redis.timeout"));

    /**
     * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
     */
    private final static boolean TEST_ON_BORROW = Boolean.valueOf(PropertyUtils
            .getRedis("redis.test_on_borrow"));

    /**
     * Redis服务器连接池
     */
    private static JedisPool jedisPool = null;

    /** redis过期时间,以秒为单位 一小时 */
    //public final static int DEFAULT_TIME = Integer.parseInt(PropertyUtils.getRedis("redis.default_overtime"));

    /**
     * 是否使用redis 空白时，为不使用
     */
    public final static String REDIS_USED = PropertyUtils.getRedis("redis.used");

    /**
     * 用户登录验证TOKEN值
     */
    public final static String TOKEN_LOGIN = "TOKEN_LOGIN";

    /**
     * 点击提交TOKEN值
     */
    public final static String TOKEN_SUBMIT = "TOKEN_SUBMIT";

    /**
     * 用户信息存放到SESSION的KEY值
     */
    public final static String SESSION_KEY_USER = "SESSION_KEY_USER";

    /**
     * 应用程序级共享KEY值
     */
    public static final Map<String, String> keyMap = new HashMap<String, String>();

    /**
     * 初始化Redis连接池
     */
    private static boolean initialPool() {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);

            jedisPool = new JedisPool(config, ADDR_ARRAY, PORT_MASTER, TIMEOUT);


        } catch (Exception e) {
            logger.error("第一个Redis连接池错误 : " + e.getMessage());
            try {
                // 如果第一个IP异常，则访问第二个IP
				/*JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_ACTIVE);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(MAX_WAIT);
				config.setTestOnBorrow(TEST_ON_BORROW);
				jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[1],
						PORT_SLAVE, TIMEOUT);*/
            } catch (Exception e2) {
                logger.error("第二个Redis连接池错误 : " + e2.getMessage());
            }
        }
        return true;
    }

    /**
     * 在多线程环境同步初始化
     */
    private static void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    @SuppressWarnings("deprecation")
    private static void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 同步获取Jedis实例
     *
     * @return Jedis
     */
    public synchronized static Jedis getJedis() {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return null;
        }
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
            if (jedis != null) {
                jedis.auth(AUTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("取得Jedis错误 : " + e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return jedis;
    }

    /**
     * 同步获取Jedis实例
     *
     * @return Jedis
     */
    public static boolean searchObjectByKeyFuzzy(String key, final RedisCallBack callBack) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
            if (jedis != null) {
                jedis.auth(AUTH);
            }
            callBack.exec(jedis, key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Jedis call back错误 : " + e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return true;
    }

    /**
     * 向redis中，写入String值
     *
     * @param key     KEY值
     * @param seconds 过期时间（以秒为单位,-1为永久）
     * @param value   VALUE值
     */
    public static boolean setRedisString(String key, String value, int seconds) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || jedis.exists(key)) {
                return false;
            }
            if (seconds == -1) {
                jedis.set(key, value);
            } else {
                jedis.setex(key, seconds, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("写入redis String值出错 : " + e.getMessage());

        }
        return false;
    }

    /**
     * 向redis中，写入String值
     *
     * @param key     KEY值
     * @param seconds 过期时间（以秒为单位,-1为永久）
     * @param value   VALUE值
     */
    public static String getRedisString(String key) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return null;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return null;
            }
            return jedis.get(key);
        } catch (Exception e) {

            logger.error("获取 redis String值出错 : " + e.getMessage());

        }
        return null;
    }


    /**
     * 向某个key值push string
     *
     * @param key   KEY值
     * @param value VALUE值
     */
    public static boolean listPushString(String key, String value) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null) {
                return false;
            }
            jedis.lpush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("写入redis String值出错 : " + e.getMessage());

        }
        return false;
    }

    /**
     * 从redis中，读取自用String List值
     *
     * @param key KEY值
     * @return VALUE值
     */
    public static List<String> getStringList(String key, int start, int end) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return null;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return null;
            }
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("获取String List值出错 : " + e.getMessage());

        }
        return null;
    }


    /**
     * 向某个key值push Object
     *
     * @param key   KEY值
     * @param value VALUE值
     */
    public static boolean listPushObj(String key, Object value) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null) {
                return false;
            }
            jedis.lpush(key.getBytes(), SerializeUtils.serialize(value));
            return true;
        } catch (Exception e) {
            logger.error("写入redis Object值出错 : " + e.getMessage());

        }
        return false;
    }


    /**
     * 从redis中，读取String值
     *
     * @param request HttpServletRequest
     * @param key     KEY值
     * @return VALUE值
     */
    public static List<Object> getObjectList(String key, int start, int end) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return null;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return null;
            }
            List<Object> objList = new ArrayList<Object>();
            List<byte[]> objByteList = jedis.lrange(key.getBytes(), start, end);
            for (byte[] objByte : objByteList) {
                Object objs = SerializeUtils.unserialize((byte[]) objByte);
                objList.add(objs);
            }
            return objList;
        } catch (Exception e) {
            logger.error("读取Object List值出错 : " + e.getMessage());

        }
        return null;
    }


    /**
     * 从redis中，删除String对象
     *
     * @param key KEY值
     */
    public static boolean removeString(String key) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return false;
            }
            jedis.del(key);
            return true;
        } catch (Exception e) {
            logger.error("删除redis值出错 : " + e.getMessage());

        }
        return true;
    }

    /**
     * 从redis中，删除String对象
     *
     * @param key KEY值
     */
    public static boolean removeObjOrList(String key) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return false;
            }
            jedis.del(key.getBytes());
            return false;
        } catch (Exception e) {
            logger.error("删除redis Object Or List 出错 : " + e.getMessage());

        }
        return true;
    }


    /**
     * 向redis中，写入Object值
     *
     * @param key     KEY值
     * @param seconds 过期时间（以秒为单位,-1为永久）
     * @param value   Object值
     */
    public static boolean setRedisObj(String key, int seconds, Object value) {
        if (StringUtils.isEmpty(REDIS_USED)) {
            return false;
        }
        try {
            value = value == null ? "" : value;
            if (seconds != -1) {
                getJedis().setex(key.getBytes(), seconds, SerializeUtils.serialize(value));
            } else {
                getJedis().set(key.getBytes(), SerializeUtils.serialize(value));
            }
            return true;
        } catch (Exception e) {
            logger.error("设置redis值出错 : " + e.getMessage());
            return false;
        }

    }


    /**
     * 从redis中，读取Object对象
     *
     * @param request HttpServletRequest
     * @param key     KEY值
     * @return Object对象
     */
    public static Object getRedisObj(String key) {

        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key.getBytes())) {
                return null;
            }
            Object obj = jedis.get(key.getBytes());
            if (obj == null) {
                return null;
            } else {
                obj = SerializeUtils.unserialize((byte[]) obj);
            }
            return obj;
        } catch (Exception e) {
            logger.error("读取redis值出错 : " + e.getMessage());

        }
        return null;
    }

    public static Object getRedisObj(byte[] key) {

        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return null;
            }
            Object obj = jedis.get(key);
            if (obj == null) {
                return null;
            } else {
                obj = SerializeUtils.unserialize((byte[]) obj);
            }
            return obj;
        } catch (Exception e) {
            logger.error("读取redis值出错 : " + e.getMessage());

        }
        return null;
    }


    /**
     * 从redis中，删除Object对象
     * <p>
     * KEY值
     */
    public static void removeObj(String key) {
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key.getBytes())) {
                return;
            }
            jedis.del(key.getBytes());
            return;
        } catch (Exception e) {
            logger.error("删除redis值出错 : " + e.getMessage());
        }

    }

    public static void removeObj(byte[] key) {
        try {
            Jedis jedis = getJedis();
            if (jedis == null || !jedis.exists(key)) {
                return;
            }
            jedis.del(key);
            return;
        } catch (Exception e) {
            logger.error("删除redis值出错 : " + e.getMessage());
        }

    }


}
