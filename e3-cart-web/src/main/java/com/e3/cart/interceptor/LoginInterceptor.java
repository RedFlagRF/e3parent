package com.e3.cart.interceptor;

import com.e3.commons.util.CookieUtils;
import com.e3.commons.util.E3Result;
import com.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author RedFlag
 * @Description 用户登陆处理拦截器
 * @Date 16:49 2019/2/18
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前处理,handler之前执行
        //取cookie中token
        String token = CookieUtils.getCookieValue(request, "e3_token");
        //无token,未登录状态,放行
        if (StringUtils.isBlank(token)) return true;
        //有token ,调用sso服务
        E3Result result = tokenService.getUserByToken(token);
        //没有取到token ,登陆过期,放行
        if (200 != result.getStatus()) {
            return true;
        }
        //取到用户信息,已登陆状态
        Object data = result.getData();
        //用户信息保存到request,controller只判断是否有user信息 ,放行
        request.setAttribute("user", data);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //handler执行之后,返回ModleAndView前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //完成处理,返回ModelAndView后
    }
}
