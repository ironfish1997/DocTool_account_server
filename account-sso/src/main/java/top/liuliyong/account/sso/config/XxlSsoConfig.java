package top.liuliyong.account.account.sso.config;

import top.liuliyong.account.account.sso.core.store.SsoLoginStore;
import top.liuliyong.account.common.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuxueli 2018-04-03 20:41:07
 * @update liyong.liu 2019-03-14
 */
@Configuration
public class XxlSsoConfig implements InitializingBean, DisposableBean {

    @Value("${sso.redis.address}")
    private String redisAddress;

    @Value("${sso.redis.expire.minite}")
    private int redisExpireMinite;

    @Override
    public void afterPropertiesSet() throws Exception {
        SsoLoginStore.setRedisExpireMinite(redisExpireMinite);
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

}
