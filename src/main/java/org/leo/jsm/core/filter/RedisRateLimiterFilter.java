package org.leo.jsm.core.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.leo.jsm.api.dao.RedisDao;
import org.leo.jsm.api.dao.RedisDistributedLock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.leo.jsm.core.Constants;
import org.leo.jsm.core.Message;

/**
 * @author Leo
 * @since 2018/10/09
 */
public class RedisRateLimiterFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(RedisRateLimiterFilter.class);

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Autowired
    protected Constants constants = null;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        String token = requestContext.getHeaderString("Token");
        String userId = this.redisDao.getHashMap("auth.token." + token);

        // 限制用户同一个API, 1秒之内只能访问一次
        String encPath = URLEncoder.encode(path, "utf-8");
        String key = "lock.api." + encPath + ".uid." + userId;
        Boolean tryGetLock = redisDistributedLock.setLock(key, 1000);
        if (tryGetLock) {
            return;
        } else {
            Message msg = new Message(930021,constants.messageMap.get(String.valueOf(930021)));
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(msg).build());
        }
    }

}
