package top.liuliyong.account.dao.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import top.liuliyong.account.common.response.AccountOperationResponse;
import top.liuliyong.account.common.response.StatusEnum;
import top.liuliyong.account.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
@Service
public class AccountUserDao extends AbstractUserDao<Account> {

    @Override
    protected Class getEntityClass() {
        return Account.class;
    }

    @Override
    protected String getCollection() {
        return "accounts";
    }

    @Override
    public List deleteUser(String... account_ids) {
        int count = 0;
        List<Object> result = new ArrayList<>();
        for (String account_id : account_ids) {
            try {
                mongoTemplate.remove(query(where("account_id").is(account_id)), getEntityClass(), getCollection());
            } catch (Exception e) {
                AccountOperationResponse response = new AccountOperationResponse(StatusEnum.DELETE_ACCOUNT_FAILED);
                result.add(response);
                continue;
            }
            result.add(new AccountOperationResponse(0, account_id + " has been removed", null));
        }
        return result;
    }

    /**
     * 修改账户信息
     */
    @Override
    public Account updateUser(Account account) {
        if (account == null || account.getAccount_id().trim().length() == 0) {
            return null;
        }
        Criteria condition = new Criteria();
        condition.and("account_id").is(account.getAccount_id());
        Query query = new Query(condition);
        if (mongoTemplate.findAndReplace(query, account, getCollection()) != null) {
            return account;
        }
        return null;
    }

    /**
     * 通过账号查找账号信息
     */
    public Account findByAccountId(String account_id) {
        if (account_id == null || account_id.trim().length() == 0) {
            return null;
        }
        Criteria condition = new Criteria();
        condition.and("account_id").is(account_id);
        Query query = new Query(condition);
        List<Account> resultList = mongoTemplate.find(query, Account.class, getCollection());
        if (resultList.size() == 0) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    /**
     * 搜索所有用户
     */
    public List<Account> findAll() {
        return StreamSupport.stream(super.findAll().spliterator(), false).collect(Collectors.toList());
    }

    /**
     * 冻结账户
     */
    public Account frozeAccount(String accountId) {
        Account accountToFroze = findByAccountId(accountId);
        if (accountToFroze == null) {
            return null;
        }
        //如果已经是冻结状态则直接返回
        if (!accountToFroze.getStatus()) {
            return accountToFroze;
        }
        //更改其状态并更新数据库
        accountToFroze.setStatus(false);
        return updateUser(accountToFroze);
    }

    /**
     * 解冻账户
     */
    public Account unFrozeAccount(String accountId) {
        Account accountToUnFroze = findByAccountId(accountId);
        if (accountToUnFroze == null) {
            return null;
        }
        //如果已经是可用状态则直接返回
        if (accountToUnFroze.getStatus()) {
            return accountToUnFroze;
        }
        //更改其状态并更新数据库
        accountToUnFroze.setStatus(true);
        return updateUser(accountToUnFroze);
    }
}
