package top.liuliyong.service;

import top.liuliyong.common.response.AccountOperationResponse;
import top.liuliyong.common.response.StatusEnum;
import top.liuliyong.common.util.MD5Encoder;
import top.liuliyong.dao.impl.AccountUserDao;
import top.liuliyong.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 提供账户服务
 *
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
@Service
public class AccountService {
    @Autowired
    AccountUserDao accountUserDao;

    /**
     * 新增账户
     *
     * @param account
     * @return
     */
    public AccountOperationResponse addAccount(Account account) throws UnsupportedEncodingException {
        String account_id = account.getAccount_id();
        if (account_id == null || account_id.trim().length() == 0 || account.getAccount_password() == null || account.getAccount_password().trim().length() == 0 || account.getAccount_permission() == null || account.getAccount_permission().size() == 0 || account.getArea() == null || account.getArea().trim().length() == 0
                //phone_number必须填写
                || account.getContacts() == null || account.getContacts().getPhone_number() == null) {
            return new AccountOperationResponse(StatusEnum.LACK_OF_INFORMATION);
        }
        if (accountUserDao.findByAccountId(account_id) != null) {
            return new AccountOperationResponse(StatusEnum.ALREADY_REGISTED);
        }
        //存储密码的md5格式
        String passwordMD5 = MD5Encoder.getMD5(account.getAccount_password());
        account.setAccount_password(passwordMD5);
        account.setStatus(true);
        Account resuleAcco = accountUserDao.<Account>saveUser(account);
        return new AccountOperationResponse(0, "ok", resuleAcco);
    }

    /**
     * 修改账户数据
     *
     * @param account
     * @return
     */
    public AccountOperationResponse updateAccount(Account account) throws UnsupportedEncodingException {
        if (account == null || account.getAccount_id() == null || account.getAccount_id().trim().length() == 0) {
            return new AccountOperationResponse(StatusEnum.LACK_OF_INFORMATION);
        }
        String account_id = account.getAccount_id();
        Account accountOri = accountUserDao.findByAccountId(account_id);
        if (accountOri == null) {
            return addAccount(account);
        } else {
            if (account.getAccount_password() != null) {
                accountOri.setAccount_password(MD5Encoder.getMD5(account.getAccount_password()));
            }
            if (account.getAccount_permission() != null) {
                accountOri.setAccount_permission(account.getAccount_permission());
            }
            if (account.getArea() != null && account.getArea().trim().length() != 0) {
                accountOri.setArea(account.getArea());
            }
            if (account.getContacts() != null && account.getContacts().getPhone_number() != null) {
                accountOri.setContacts(account.getContacts());
            }
            if (account.getName() != null) {
                accountOri.setName(account.getName());
            }
            if (account.getExtra_meta() != null) {
                accountOri.setExtra_meta(account.getExtra_meta());
            }
            Account resultAcc = accountUserDao.updateUser(accountOri);
            Account newAcc = accountUserDao.findByAccountId(accountOri.getAccount_id());
            return new AccountOperationResponse(0, "ok", newAcc);
        }
    }

    /**
     * 通过account_id删除账户信息，可批量删除
     *
     * @param account_ids
     * @return
     */
    public AccountOperationResponse deleteAccount(String... account_ids) {
        if (account_ids == null || account_ids.length == 0) {
            return new AccountOperationResponse(StatusEnum.LACK_OF_INFORMATION);
        }
        List result = accountUserDao.deleteUser(account_ids);
        return new AccountOperationResponse(0, "ok", result);
    }

}
