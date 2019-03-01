package com.e3.sso.service.impl;

import com.e3.commons.jedis.JedisClient;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.mapper.TbUserMapper;
import com.e3.pojo.TbUser;
import com.e3.pojo.TbUserExample;
import com.e3.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Author RedFlag
 * @Description 用户登陆Service
 * @Date 21:04 2019/2/16
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Value("${REDIS_SESSION_EXPIRES}")
    private int REDIS_SESSION_EXPIRES;
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 说明     :  用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result userLogin(String username, String password) {
        //判断用户名密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> temp = tbUserMapper.selectByExample(example);
        if (temp == null || temp.size() == 0) {
            //不存在该用户名
            return E3Result.build(E3Result.OTHER_ERROR, "用户名或密码错误");
        }
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        TbUser user = temp.get(0);
        if (!user.getPassword().equals(pwd)) {
            //不正确,返回登录失败
            return E3Result.build(E3Result.OTHER_ERROR, "用户名或密码错误");
        }
        //正确 生成token
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        //用户信息写入redis,key :token,value user
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        // 设置过期时间
        jedisClient.expire("SESSION:" + token, REDIS_SESSION_EXPIRES);
        //返回token
        return E3Result.ok(token);
    }
}
