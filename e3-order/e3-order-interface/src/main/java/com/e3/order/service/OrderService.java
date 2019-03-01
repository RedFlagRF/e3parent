package com.e3.order.service;

import com.e3.commons.util.E3Result;
import com.e3.order.pojo.OrderInfo;

public interface OrderService {
    E3Result createOrder(OrderInfo orderInfo);
}
