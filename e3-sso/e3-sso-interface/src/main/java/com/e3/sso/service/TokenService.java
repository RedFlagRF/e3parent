package com.e3.sso.service;

import com.e3.commons.util.E3Result;

/**
 * @Author RedFlag
 * @Description  根据同ken查询用户信息
 * @Date 11:22 2019/2/17
 */
public interface TokenService {
    /**
    *  说明     :  通过token查询用户是否登陆
      * @param      token
    * @return  :  com.e3.commons.util.E3Result
    */
    E3Result getUserByToken(String token);
}
