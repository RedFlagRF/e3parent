package com.e3.sso.controller;

import com.e3.commons.util.E3Result;
import com.e3.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author RedFlag
 * @Description 用户token校验登陆信息
 * @Date 11:45 2019/2/17
 */
@Controller
public class TokenController {
    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @CrossOrigin
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.POST)
    @ResponseBody
    public E3Result getUserByToken(@PathVariable String token) {
        return tokenService.getUserByToken(token);
    }
}
