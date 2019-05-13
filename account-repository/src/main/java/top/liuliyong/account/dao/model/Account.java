package top.liuliyong.account.dao.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

/**
 * @Author liyong.liu
 * @Date 2019/3/11
 **/
@Document(collection = "accounts")
@Data
public class Account extends User {
    @Indexed(unique = true)
    private String account_id;
    private String account_password;
    private Boolean status;
    private List<String> account_permission;

    public Account(String name, String area, Contacts contacts, String account_id, String account_password, List<String> account_permission, Object extra_meta) {
        super(null, name, area, contacts, extra_meta);
        this.account_id = account_id;
        this.status = true;
        this.account_password = account_password;
        this.account_permission = account_permission;
    }

    public Account() {
        super();
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return Objects.equals(account_id, account.account_id) && Objects.equals(account_password, account.account_password) && Objects.equals(status, account.status) && Objects.equals(account_permission, account.account_permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account_id, account_password, status, account_permission);
    }
}
