package top.liuliyong.account.sso.core.store;

import top.liuliyong.account.sso.config.SSOConf;
import top.liuliyong.account.common.util.JedisUtil;
import top.liuliyong.account.dao.model.XxlSsoUser;

/**
 * local login store
 *
 * @author xuxueli 2018-04-02 20:03:11
 * @update liyong.liu 2019-03-14
 */
public class SsoLoginStore {

    private static int redisExpireMinite = 30;    // 30 minite

    public static void setRedisExpireMinite(int redisExpireMinite) {
        if (redisExpireMinite < 30) {
            redisExpireMinite = 30;
        }
        SsoLoginStore.redisExpireMinite = redisExpireMinite;
    }

    public static int getRedisExpireMinite() {
        return redisExpireMinite;
    }

    /**
     * get
     *
     * @param storeKey
     * @return
     */
    public static XxlSsoUser get(String storeKey) {
        String redisKey = redisKey(storeKey);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            XxlSsoUser xxlUser = (XxlSsoUser) objectValue;
            return xxlUser;
        }
        return null;
    }

    /**
     * remove
     *
     * @param storeKey
     */
    public static long remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        return JedisUtil.del(redisKey);
    }

    /**
     * put
     *
     * @param storeKey
     * @param xxlUser
     */
    public static void put(String storeKey, XxlSsoUser xxlUser) {
        String redisKey = redisKey(storeKey);
        JedisUtil.setObjectValue(redisKey, xxlUser, redisExpireMinite * 60);  // minite to second
    }

    /**
     * put into Redis with Custom expire mitute
     *
     * @param storeKey
     * @param xxlUser
     * @param expireMinite
     */
    public static void put(String storeKey, XxlSsoUser xxlUser, int expireMinite) {
        String redisKey = redisKey(storeKey);
        if (expireMinite == 0) {
            expireMinite = redisExpireMinite;
        }
        JedisUtil.setObjectValue(redisKey, xxlUser, expireMinite * 60);  // minite to second
    }

    private static String redisKey(String sessionId) {
        return SSOConf.SSO_SESSIONID.concat("#").concat(sessionId);
    }

}
