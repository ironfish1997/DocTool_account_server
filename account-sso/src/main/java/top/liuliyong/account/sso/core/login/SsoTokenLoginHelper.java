package top.liuliyong.account.sso.core.login;

import top.liuliyong.account.sso.core.store.SsoLoginStore;
import top.liuliyong.account.sso.core.store.SsoSessionIdHelper;
import top.liuliyong.account.sso.config.SSOConf;
import top.liuliyong.account.model.XxlSsoUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liyong.liu 2019-03-14
 */
public class SsoTokenLoginHelper {

    /**
     * client login
     *
     * @param sessionId
     * @param xxlUser
     */
    public static void login(String sessionId, XxlSsoUser xxlUser) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, xxlUser, xxlUser.getExpireMinite());
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static Long logout(String sessionId) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        return SsoLoginStore.remove(storeKey);
    }

    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(SSOConf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static XxlSsoUser loginCheck(String sessionId) {
        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        XxlSsoUser xxlUser = SsoLoginStore.get(storeKey);
        if (xxlUser != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if (xxlUser.getVersion().equals(version)) {
                return xxlUser;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static XxlSsoUser loginCheck(HttpServletRequest request) {
        String headerSessionId = request.getHeader(SSOConf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }


}
