package com.e3.order.service;

import com.e3.commons.jedis.JedisClient;
import com.e3.commons.util.E3Result;
import com.e3.mapper.TbItemMapper;
import com.e3.mapper.TbOrderItemMapper;
import com.e3.mapper.TbOrderMapper;
import com.e3.mapper.TbOrderShippingMapper;
import com.e3.order.pojo.OrderInfo;
import com.e3.order.service.OrderService;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbOrderItem;
import com.e3.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${ORDER_ID_INCR_KEY}")
    private String ORDER_ID_INCR_KEY;
    @Value("${ORDER_DETAIL_INCR_KEY}")
    private String ORDER_DETAIL_INCR_KE;
    @Value("${ORDER_ID_INCR_START}")
    private String ORDER_ID_INCR_START;
    private final TbOrderMapper orderMapper;
    private final TbOrderItemMapper orderItemMapper;
    private final TbOrderShippingMapper orderShippingMapper;
    private final JedisClient jedisClient;
    private final TbItemMapper itemMapper;


    @Autowired
    public OrderServiceImpl(TbOrderMapper orderMapper, TbOrderItemMapper orderItemMapper, TbOrderShippingMapper orderShippingMapper, JedisClient jedisClient, TbItemMapper itemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderShippingMapper = orderShippingMapper;
        this.jedisClient = jedisClient;
        this.itemMapper = itemMapper;
    }


    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //使用redis的increase生成订单号
        if (!jedisClient.exists(ORDER_ID_INCR_KEY)) {
            jedisClient.set(ORDER_ID_INCR_KEY, ORDER_ID_INCR_START);
        }
        String orderId = jedisClient.incr(ORDER_ID_INCR_KEY).toString();

        //订单明细插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        long totalFree = 0L;
        Long postFee = Long.valueOf(orderInfo.getPostFee());
        totalFree += postFee;
        for (TbOrderItem orderItem : orderItems) {
            String orderDetailId = jedisClient.incr(ORDER_DETAIL_INCR_KE).toString();
            orderItem.setId(orderDetailId);
            orderItem.setOrderId(orderId);
            //以防万一,从数据库查询价格
            TbItem temp = itemMapper.selectByPrimaryKey(Long.valueOf(orderItem.getItemId()));
            orderItem.setPrice(temp.getPrice());
            //重新计算价格
            long fee = orderItem.getPrice() * orderItem.getNum();
            orderItem.setTotalFee(fee);
            //重新计算总价
            totalFree += fee;
            orderItemMapper.insert(orderItem);
        }
        orderInfo.setPayment(String.valueOf(totalFree));
        //补全orderInfo属性
        orderInfo.setOrderId(orderId);
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderInfo);
        //订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        //返回包含订单号的结果
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("totalFree", String.valueOf(totalFree));
        return E3Result.ok(map);
    }
}
