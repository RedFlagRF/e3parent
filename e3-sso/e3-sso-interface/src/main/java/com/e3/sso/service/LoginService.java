package com.e3.sso.service;

import com.e3.commons.util.E3Result;

public interface LoginService {
    /**
     * 说明     :  用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result userLogin(String username, String password);
}
