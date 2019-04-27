package top.liuliyong.account.account.sso.service;


import top.liuliyong.account.account.sso.core.result.ReturnT;

import java.io.UnsupportedEncodingException;

public interface UserService<T> {

    public ReturnT<T> findUser(String username, String password) throws UnsupportedEncodingException;

}
