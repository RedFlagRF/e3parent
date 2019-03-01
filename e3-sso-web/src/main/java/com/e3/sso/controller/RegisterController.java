package com.e3.sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.e3.commons.util.E3Result;
import com.e3.pojo.TbUser;
import com.e3.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author RedFlag
 * @Description 注册Controller
 * @Date 16:44 2019/2/16
 */
@Controller
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable int type) {
        return registerService.checkData(param, type);
    }

    @RequestMapping(value = "/user/register" ,method = RequestMethod.POST)
    @ResponseBody
    public E3Result userRegister(TbUser user) {
        return registerService.userRegister(user);
    }
}
