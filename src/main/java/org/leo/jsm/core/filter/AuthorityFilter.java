package org.leo.jsm.core.filter;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.leo.jsm.api.utils.PropertyUtils;
import org.leo.jsm.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import org.leo.jsm.api.dao.RedisCacheDao;
import org.leo.jsm.api.dao.RedisDao;
import org.leo.jsm.core.Constants;
import org.leo.jsm.core.Message;

public class AuthorityFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(AuthorityFilter.class);

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private RedisCacheDao redisCacheDao;

    @Autowired
    protected Constants constants = null;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        logger.info(path);
        if (isOpenPath(path)) {
            return;
        }

        String token = requestContext.getHeaderString("Token");
        String userid = this.verify(token);
        logger.info("token:" + token + "; userid:" + userid);
        if (null == userid) {
            Message msg = new Message(40100, constants.messageMap.get(String.valueOf(40100)));
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(msg).build());
        }

        //lastLogin
        String key = PropertyUtils.getConfig("store.key") + "." + userid;
        long storeTime = Long.valueOf(PropertyUtils.getConfig("store.time"));
        logger.info("key===" + key + "   storeTime====" + storeTime);
        String lastLogin = redisCacheDao.getCache(key);
        if (StringUtils.isNotBlank(lastLogin)) {
            redisCacheDao.deleteCache(key);
        }
        redisCacheDao.putCache(key, String.valueOf(Utils.getCurrentTimestamp()), storeTime);
        logger.info("lastLogin=" + redisCacheDao.getCache(key));

        requestContext.getHeaders().putSingle("X_LOGIN_USERID", userid);
    }

    private boolean isOpenPath(String path) {
        Set<String> openUrl = constants.getOpenUrlPath();
        for (String url : openUrl) {
            logger.info(url);
            Pattern pattern = Pattern.compile(url, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(path);
            logger.info("------" + matcher.matches());
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    private String verify(String token) {
        String userid = this.redisDao.getHashMap("auth.token." + token);
        return userid;
    }

}
