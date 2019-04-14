package top.liuliyong.dao.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import top.liuliyong.common.response.AccountOperationResponse;
import top.liuliyong.common.response.StatusEnum;
import top.liuliyong.model.Account;

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
     *
     * @param account
     * @return
     */
    @Override
    public Account updateUser(Account account) {
        if (account == null || account.getAccount_id().trim().length() == 0) {
            return null;
        }
        Criteria condition = new Criteria();
        condition.and("account_id").is(account.getAccount_id());
        Query query = new Query(condition);
        return mongoTemplate.findAndReplace(query, account, getCollection());
    }

    /**
     * search for the account information by account_id
     *
     * @param account_id
     * @return
     */
    public Account findByAccountId(String account_id) {
        if (account_id == null || account_id.trim().length() == 0) {
            return null;
        }
        Criteria condition = new Criteria();
        condition.and("account_id").is(account_id);
        Query query = new Query(condition);
        List<Account> resultList = mongoTemplate.find(query, Account.class, getCollection());
        if (resultList == null || resultList.size() == 0) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    /**
     * search all users
     * @return
     */
    public List<Account> findAll(){
       return StreamSupport.stream(super.findAll().spliterator(), false) .collect(Collectors.toList());
    }
}
