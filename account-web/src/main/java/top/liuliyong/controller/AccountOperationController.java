package top.liuliyong.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.liuliyong.common.response.AccountOperationResponse;
import top.liuliyong.interceptor.annotation.CheckSessionId;
import top.liuliyong.interceptor.annotation.NeedAdminAuth;
import top.liuliyong.model.Account;
import top.liuliyong.service.AccountService;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(path = "/account")
@Validated
@Api(value = "Account", description = "账号操作")
public class AccountOperationController {
    private static Logger logger = LoggerFactory.getLogger(AccountOperationController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "", method = {RequestMethod.POST})
    @ApiOperation(value = "新增用户账户")
    public AccountOperationResponse addAccount(@RequestBody Account account, HttpServletResponse response) throws UnsupportedEncodingException {
        AccountOperationResponse result = accountService.addAccount(account);
        response.setStatus(200);
        logger.warn("收到新增用户账户请求，请求参数为==>" + JSON.toJSONString(account));
        return result;
    }

    @RequestMapping(value = "", method = {RequestMethod.PUT})
    @ApiOperation(value = "修改用户账户信息")
    @CheckSessionId
    public AccountOperationResponse updateAccount(@RequestHeader String session_id, @RequestBody Account account, HttpServletResponse response) throws UnsupportedEncodingException {
        AccountOperationResponse result = null;
        result = accountService.updateAccount(account);
        response.setStatus(200);
        logger.warn("收到修改用户账户请求，请求参数为==>" + account);
        return result;
    }

    @RequestMapping(value = "", method = {RequestMethod.DELETE})
    @ApiOperation(value = "删除账户")
    @CheckSessionId
    @NeedAdminAuth
    @ApiImplicitParams({@ApiImplicitParam(name = "account_id", value = "账号id", required = true, dataType = "string"),})
    public AccountOperationResponse deleteAccount(@RequestHeader String session_id, @RequestParam String account_id, HttpServletResponse response) {
        AccountOperationResponse result = accountService.deleteAccount(account_id);
        response.setStatus(200);
        logger.warn("收到删除用户账户请求，请求删除的账号为==>" + account_id);
        return result;
    }
}