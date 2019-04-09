package top.liuliyong.sso_server.service;


import top.liuliyong.sso_server.core.result.ReturnT;

import java.io.UnsupportedEncodingException;

public interface UserService<T> {

    public ReturnT<T> findUser(String username, String password) throws UnsupportedEncodingException;

}
