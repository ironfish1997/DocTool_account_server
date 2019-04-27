package top.liuliyong.account.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.liuliyong.account.dao.impl.AccountUserDao;
import top.liuliyong.account.model.Account;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019/3/11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountUserServiceTest {

    @Autowired
    AccountUserDao accountDao;

    @Test
    public void testAccountDao() {
        Account a = accountDao.findByAccountId("1481980097@qq.com");
        Assert.assertNotNull(a);
    }

    @Test
    public void testSaveUser() {
//        Patient patient1 = new PatientBuilder().init("patient1", "中国").setContacts("patientWechat", "1481728392", "13333333333", "1481980029").build();
//        Account account1 = new AccountBuilder().init("1481980097@qq.com", "123456", null).setArea("中国").setName("Helen").setContacts("abcdef", "1481980097", "13232323232", "1481980097@qq.com").build();
//        Patient patientRes = patientDao.<Patient>saveUser(patient1);
//        Account accountRes = accountDao.<Account>saveUser(account1);
//        Assert.assertTrue(patientRes.equals(patient1));
//        Assert.assertTrue(accountRes.equals(account1));
    }

    @Test
    public void testFindAllAccount() {
        List<Account> a = (List<Account>) accountDao.findAll();
        Assert.assertNotNull(a);
    }

    @Test
    public void testFindOne() {
        Account a = (Account) accountDao.findById("5c8771975285b8267fb2968b").get();
        Assert.assertNotNull(a);
    }
}
