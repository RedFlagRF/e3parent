package com.e3.order.pojo;

import com.e3.pojo.TbOrder;
import com.e3.pojo.TbOrderItem;
import com.e3.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;
/**
 * @Author RedFlag
 * @Description  商品订单接收pojo
 * @Date 11:46 2019/2/20
 */
public class OrderInfo extends TbOrder implements Serializable {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
