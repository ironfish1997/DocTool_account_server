package top.liuliyong.account.dao.model.builder;

import top.liuliyong.account.dao.model.Account;
import top.liuliyong.account.dao.model.Contacts;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
public class AccountBuilder {
    private String account_id;
    private String account_password;
    private List<String> account_permission;
    private String name;
    private String area;
    Contacts contacts;
    Object extra_meta;

    public AccountBuilder init(String account_id, String account_password, List<String> account_permission) {
        this.account_id = account_id;
        this.account_password = account_password;
        this.account_permission = account_permission;
        return this;
    }

    public String getAccount_id() {
        return account_id;
    }

    public AccountBuilder setAccount_id(String account_id) {
        this.account_id = account_id;
        return this;
    }

    public String getAccount_password() {
        return account_password;
    }

    public AccountBuilder setAccount_password(String account_password) {
        this.account_password = account_password;
        return this;
    }

    public List<String> getAccount_permission() {
        return account_permission;
    }

    public AccountBuilder setAccount_permission(List<String> account_permission) {
        this.account_permission = account_permission;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getArea() {
        return area;
    }

    public AccountBuilder setArea(String area) {
        this.area = area;
        return this;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public AccountBuilder setContacts(String wechat, String qq, String phone_number, String email) {
        Contacts contacts = new Contacts(wechat, qq, phone_number, email);
        this.contacts = contacts;
        return this;
    }

    public Object getExtra_meta() {
        return extra_meta;
    }

    public AccountBuilder setExtra_meta(Object extra_meta) {
        this.extra_meta = extra_meta;
        return this;
    }

    public Account build() {
        return new Account(name, area, contacts, account_id, account_password, account_permission, extra_meta);
    }
}
