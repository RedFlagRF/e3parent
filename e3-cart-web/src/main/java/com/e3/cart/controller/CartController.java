package com.e3.cart.controller;

import com.e3.cart.service.CartService;
import com.e3.commons.util.CookieUtils;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbUser;
import com.e3.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author RedFlag
 * @Description 购物车Controller
 * @Date 20:18 2019/2/17
 */
@Controller
public class CartController {
    @Value("${CART_COOKIE_EXPIRE}")
    private int CART_COOKIE_EXPIRE;
    private final ItemService itemService;
    private final CartService cartService;

    @Autowired
    public CartController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
        //request取user
        TbUser user = (TbUser) request.getAttribute("user");
        //非空,存 redis
        if (null != user) {
            //保存服务端
            cartService.addCart(user.getId(), itemId, num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //cookie取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品在商品列表是否存在
        boolean flag = false;
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().equals(itemId)) {
                //找到商品,数量添加.flag 改true
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                //结束循环
                break;
            }
        }
        if (!flag) {
            //不存在,根据ID查询商品,得到TbItem对象
            TbItem item = itemService.getItemById(itemId);
            // 追加商品
            item.setNum(num);
            String img = item.getImage();
            if (StringUtils.isNotBlank(img)) {
                item.setImage(item.getImage().split(",")[0]);
            }
            cartList.add(item);
        }
        // 写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), CART_COOKIE_EXPIRE, true);
        //返回添加成功页面
        return "cartSuccess";
    }

    /**
     * 说明     :  从cookie获取商品列表
     *
     * @param request request
     * @return :  java.util.List<com.e3.pojo.TbItem>
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isBlank(json)) {//判断是否为空
            //空
            return new ArrayList<>();
        }
        //非空
        return JsonUtils.jsonToList(json, TbItem.class);
    }

    /**
     * 说明     : 从cookie取购物车列表,跳转到购物车页面
     *
     * @param request request
     * @return :  java.lang.String
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response) {
        //从cookie取购物车列表
        List<TbItem> list = getCartListFromCookie(request);
        //判断是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //登陆合并购物车
            cartService.mergeCart(user.getId(), list);
            //删除cookie
            CookieUtils.deleteCookie(request, response,"cart" );
            //获取购物车
            list = cartService.getCartList(user.getId());
        }
        //把列表传递给页面
        request.setAttribute("cartList", list);
        //返回逻辑视图
        return "cart";
    }

    /**
     * 说明     :  更新购物车数量
     *
     * @param itemId   商品ID
     * @param num      商品数量
     * @param request  request
     * @param response response
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
       //判断用户是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
        //如果登陆 ,调用cartService
            return cartService.updateCart(user.getId(), itemId, num);
    }
        //未登录
        //cookie取商品列表
        List<TbItem> list = getCartListFromCookie(request);
        //遍历商品列表,取对应商品
        for (TbItem item : list) {
            if (item.getId().equals(itemId)) {
                item.setNum(num);
                break;
            }
        }
        //商品列表存入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_COOKIE_EXPIRE, true);
        //返回
        return E3Result.ok();
    }

    /**
     * 说明     :  删除通过id购物车中商品
     *
     * @param itemId   商品ID
     * @param request  request
     * @param response response
     * @return :  java.lang.String
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String delItemById(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //如果登陆 ,调用cartService
            cartService.delCart(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        //未登录
        //cookie取商品列表
        List<TbItem> list = getCartListFromCookie(request);
        //遍历商品列表,删除应商品
        for (TbItem item : list) {
            if (item.getId().equals(itemId)) {
                list.remove(item);
                break;
            }
        }
        //商品列表存入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_COOKIE_EXPIRE, true);
        //返回
        return "redirect:/cart/cart.html";
    }
}
