package com.e3.order.interceptor;

import com.e3.cart.service.CartService;
import com.e3.commons.util.CookieUtils;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbUser;
import com.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author RedFlag
 * @Description 用户登陆拦截器
 * @Date 21:43 2019/2/19
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;
    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie取token判断用户是否存在
        String token = CookieUtils.getCookieValue(request, "e3_token");
        //不存在,未登录,跳转登陆页面,登陆后跳回当前请求url
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //存在token,sso服务查询
        E3Result result = tokenService.getUserByToken(token);
        if (200 != result.getStatus()) {
            //不存在,登陆过期,跳转登陆页面,登陆后跳回当前请求url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        TbUser user = (TbUser) result.getData();
        //查询存在, 登陆状态,用户写入request
        request.setAttribute("user", user);
        //判断cookie是否有购物车,
        String cartList = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(cartList)) {
            // 有,合并购物车到服务端,删除cookie 购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartList, TbItem.class));
            CookieUtils.deleteCookie(request,response,"cart");
        }
        //放行
        return true;
    }
}
