package com.e3.order.controller;

import com.e3.cart.service.CartService;
import com.e3.commons.util.E3Result;
import com.e3.order.pojo.OrderInfo;
import com.e3.order.service.OrderService;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbOrderItem;
import com.e3.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author RedFlag
 * @Description 订单Controller
 * @Date 21:14 2019/2/19
 */
@Controller
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public OrderController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @RequestMapping("/order/order-cart")
    public String showOrderCart(@RequestAttribute("user") TbUser user, HttpServletRequest request, Model model) {
        //request 取用户 ID
        Long id = user.getId();
        //根据ID获取收货地址列表

        //取支付方式列表

        //通过ID获取购物车列表
        List<TbItem> cartList = cartService.getCartList(id);
        //设置值
        model.addAttribute("cartList", cartList);
//        request.setAttribute("cartList", cartList);
        //返回
        return "order-cart";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model, @RequestAttribute("user") TbUser user) {

        //把用户信息添加到orderInfo
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用服务生成id
        E3Result result = orderService.createOrder(orderInfo);
        if (200 == result.getStatus()) {
            for (TbOrderItem orderItem : orderInfo.getOrderItems()) {
                //删除购物车中已购买商品
                cartService.delCart(user.getId(), Long.parseLong(orderItem.getItemId()));
            }
        }
        Map map = (Map) result.getData();
        model.addAttribute("orderId", map.get("orderId"));
        model.addAttribute("payment",map.get("totalFree"));
        //返回逻辑视图
        return "success";
    }
}
