package top.liuliyong.account.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liuliyong.account.common.response.StatusEnum;
import top.liuliyong.account.common.util.MD5Encoder;
import top.liuliyong.account.dao.impl.AccountUserDao;
import top.liuliyong.account.model.Account;
import top.liuliyong.account.sso.core.result.ReturnT;
import top.liuliyong.account.sso.service.UserService;

/**
 * @Author liyong.liu
 * @Date 2019/3/13
 **/
@Service("MongoUserService")
public class MongoUserService implements UserService {
    @Autowired
    AccountUserDao accountUserDao;

    @Override
    public ReturnT<Account> findUser(String account_id, String account_password) {
        if (account_id == null || account_password == null) {
            return new ReturnT<Account>(StatusEnum.LACK_OF_INFORMATION);
        }
        Account userInfo = accountUserDao.findByAccountId(account_id);
        if (userInfo == null) {
            return new ReturnT<Account>(StatusEnum.LACK_OF_INFORMATION);
        }
        if (userInfo.getAccount_password().equals(MD5Encoder.getMD5(account_password))) {
            return new ReturnT<Account>(userInfo);
        }
        return new ReturnT<Account>(StatusEnum.WRONG_ACCOUNT_OR_PASSWORD);
    }
}
