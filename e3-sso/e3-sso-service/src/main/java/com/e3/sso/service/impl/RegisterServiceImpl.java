package com.e3.sso.service.impl;

import com.e3.commons.util.E3Result;
import com.e3.mapper.TbUserMapper;
import com.e3.pojo.TbUser;
import com.e3.pojo.TbUserExample;
import com.e3.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author RedFlag
 * @Description 用户注册Service
 * @Date 17:53 2019/2/16
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private final TbUserMapper tbUserMapper;

    @Autowired
    public RegisterServiceImpl(TbUserMapper tbUserMapper) {
        this.tbUserMapper = tbUserMapper;
    }

    /**
     * 说明     :  根据type类型查询数据是否存在
     *
     * @param param 被查询数据
     * @param type  数据类型识别码
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result checkData(String param, int type) {
        //构建查询对象
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //根据不同type设置对应查询条件
        //1 用户名,2手机号,3邮箱
        if (1 == type) {
            criteria.andUsernameEqualTo(param);
        } else if (2 == type) {
            criteria.andPhoneEqualTo(param);
        } else if (3 == type) {
            criteria.andEmailEqualTo(param);
        } else {
            //错误操作
            return E3Result.build(E3Result.OTHER_ERROR, "非法操作");
        }
        //查询
        List<TbUser> temp = tbUserMapper.selectByExample(example);
        if (temp != null && temp.size() > 0) {
            //如果非空,数据存在,返回false
            return E3Result.ok(false);
        } else {
            //空 返回true
            return E3Result.ok(true);
        }
    }

    /**
     * 说明     :  用户注册
     *
     * @param user 用户信息
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result userRegister(TbUser user) {
        try {
            //数据校验有效性
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
                return E3Result.build(E3Result.OTHER_ERROR, "用户数据不完整,注册失败");
            }
            //数据重复检测
            E3Result result = checkData(user.getUsername(), 1);
            if (!(boolean) result.getData()) {
                return E3Result.build(E3Result.OTHER_ERROR, "用户名已被注册,注册失败");
            }
            result = checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return E3Result.build(E3Result.OTHER_ERROR, "手机号已被注册,注册失败");
            }
//            result = checkData(user.getEmail(), 3);
//            if (!(boolean) result.getData()) {
//                return E3Result.build(E3Result.OTHER_ERROR, "邮箱已被注册,注册失败");
//            }
            //补全属性
            user.setCreated(new Date());
            user.setUpdated(new Date());
            //密码加密
            String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(password);
            //插入数据
            tbUserMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(E3Result.INSERT_ERROR, "注册失败,请稍后重试");
        }
        //返回成功
        return E3Result.ok();
    }
}
