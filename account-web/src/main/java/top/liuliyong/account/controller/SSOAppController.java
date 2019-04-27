package top.liuliyong.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.liuliyong.common.response.AccountOperationResponse;
import top.liuliyong.common.response.StatusEnum;
import top.liuliyong.model.Account;
import top.liuliyong.model.XxlSsoUser;
import top.liuliyong.account.sso.annotation.SsoServer;
import top.liuliyong.account.sso.config.SSOConf;
import top.liuliyong.account.sso.core.login.SsoTokenLoginHelper;
import top.liuliyong.account.sso.core.result.ReturnT;
import top.liuliyong.account.sso.core.store.SsoSessionIdHelper;
import top.liuliyong.account.sso.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * sso_server
 *
 * @author liyong.liu   2019-03-14
 */
@RestController
@RequestMapping("/account")
@Validated
@Api(value = "SSO", description = "单点登录中心")
public class SSOAppController {

    @Autowired
    @Qualifier("MongoUserService")
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(SSOAppController.class);


    /**
     * Login
     *
     * @return
     */
    @PostMapping(value = SSOConf.SSO_LOGIN)
    @ResponseBody
    @ApiOperation(value = "用户登录")
    @SsoServer
    public Object login(@RequestBody Account userInfo, @RequestHeader boolean remember) throws UnsupportedEncodingException {
        logger.warn("收到登录请求，请求参数为==>" + JSON.toJSONString(userInfo));
        if (userInfo == null) {
            return new AccountOperationResponse(StatusEnum.LACK_OF_INFORMATION);
        }
        String username = userInfo.getAccount_id();
        String password = userInfo.getAccount_password();
        if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) {
            return new AccountOperationResponse(StatusEnum.LACK_OF_INFORMATION);
        }
        // valid login
        ReturnT<Account> result = userService.findUser(username, password);
        if (result == null || result.getCode() != ReturnT.SUCCESS_CODE) {
            return new AccountOperationResponse(result == null ? -10000 : result.getCode(), result.getMsg() != null ? result.getMsg() : "invalid login", result == null ? "" : result.getMsg());
        }

        //判断账户是否已经被冻结
        if (!result.getData().getStatus()) {
            return new AccountOperationResponse(StatusEnum.FROZEN_ACCOUNT);
        }

        // 1、make xxl-sso user
        XxlSsoUser xxlUser = new XxlSsoUser();
        xxlUser.setUserid(String.valueOf(result.getData().getId()));
        xxlUser.setUsername(result.getData().getAccount_id());
        xxlUser.setName(result.getData().getName());
        xxlUser.setAccount_id(result.getData().getAccount_id());
        xxlUser.setAccount_permission(result.getData().getAccount_permission());
        xxlUser.setArea(result.getData().getArea());
        xxlUser.setContacts(result.getData().getContacts());
        xxlUser.setExtra_meta(result.getData().getExtra_meta());
        xxlUser.setId(result.getData().getId());
        xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        xxlUser.setExpireMinite(remember ? 7200 : 15);
        xxlUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);

        // 3、login, store storeKey
        SsoTokenLoginHelper.login(sessionId, xxlUser);

        Map resultMap = new HashMap();

        resultMap.put("session_id", sessionId);

        // 4、return sessionId
        return new AccountOperationResponse(0, "ok", resultMap);
    }


    /**
     * Logout
     *
     * @param sessionId
     * @return
     */
    @PostMapping(value = SSOConf.SSO_LOGOUT)
    @ResponseBody
    @ApiOperation(value = "注销用户")
    @SsoServer
    public Object logout(@RequestHeader("session_id") String sessionId) {
        logger.warn("收到注销请求，请求参数为==>" + JSON.toJSONString(sessionId));
        // logout, remove storeKey
        Long result_id = SsoTokenLoginHelper.logout(sessionId);
        if (result_id == null || result_id == 0) {
            return new AccountOperationResponse(StatusEnum.NOT_ONLINE);
        }
        return new AccountOperationResponse(0, "ok", null);
    }

    /**
     * logincheck
     *
     * @param sessionId
     * @return
     */
    @PostMapping(value = SSOConf.SSO_LOGINCHECK)
    @ResponseBody
    @ApiOperation(value = "sessionId检查")
    @SsoServer
    public Object logincheck(@RequestHeader("session_id") String sessionId) {
        logger.warn("收到登录状态检查请求，请求参数为：" + sessionId);

        // logout
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return new AccountOperationResponse(StatusEnum.NOT_ONLINE);
        }
        return new AccountOperationResponse(0, "ok", xxlUser);
    }

}