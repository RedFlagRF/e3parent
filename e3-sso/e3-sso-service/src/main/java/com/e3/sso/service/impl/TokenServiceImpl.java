package com.e3.sso.service.impl;

import com.e3.commons.jedis.JedisClient;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.pojo.TbUser;
import com.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * @Author RedFlag
 * @Description  根据token查询用户登陆信息
 * @Date 12:01 2019/2/17
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Value(("${REDIS_SESSION_EXPIRES}"))
    private int REDIS_SESSION_EXPIRES;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 说明     :  通过token查询用户是否登陆
     *
     * @param token
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result getUserByToken(String token) {
        //redis查询用户信息
        String session = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(session)) {
            //不存在,返回未登录信息
            return E3Result.build(E3Result.Login_ERROR, "用户登陆信息过期,请重新登陆!");
        }
        //存在,续期token
        jedisClient.expire("SESSION:" + token, REDIS_SESSION_EXPIRES);
        //取User
        TbUser user = JsonUtils.jsonToPojo(session, TbUser.class);
        //返回
        return E3Result.ok(user);
    }
}
