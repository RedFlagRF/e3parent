package com.e3.sso.controller;

import com.e3.commons.util.CookieUtils;
import com.e3.commons.util.E3Result;
import com.e3.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author RedFlag
 * @Description 用户注册Controller
 * @Date 20:34 2019/2/16
 */
@Controller
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String login(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result usrLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        E3Result result = loginService.userLogin(username, password);
        //判断是否登陆成功,成功,
        if (200 == result.getStatus()) {
            //获取token
            String token = result.getData().toString();
            //token存入cookie
            CookieUtils.setCookie(request, response, "e3_token", token);
        }
        return result;
    }
}
