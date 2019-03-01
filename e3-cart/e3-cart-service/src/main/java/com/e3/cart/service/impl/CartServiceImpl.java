package com.e3.cart.service.impl;

import com.e3.cart.service.CartService;
import com.e3.commons.jedis.JedisClient;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.mapper.TbItemMapper;
import com.e3.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Value("${REDIS_CART_PRE}")
    public String REDIS_CART_PRE;
    private final JedisClient jedisClient;
    private final TbItemMapper itemMapper;

    @Autowired
    public CartServiceImpl(JedisClient jedisClient, TbItemMapper itemMapper) {
        this.jedisClient = jedisClient;
        this.itemMapper = itemMapper;
    }

    /**
     * 说明     :  根据userid 在redis 存入购物车
     *
     * @param userId 用户id
     * @param itemId 商品ID
     * @param num    商品数量
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //redis查询商品是否存在
        Boolean aBoolean = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (aBoolean) {
            //存在,添加数量
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(item.getNum() + num);
            //写回redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
            //返回
            return E3Result.ok();
        }
        //不存在,查询商品,
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //设置商品数量
        item.setNum(num);
        //设置商品图片
        String image = item.getImage();
        if (StringUtils.isNotBlank(image)) {
            item.setImage(item.getImage().split(",")[0]);
        }
        //写回redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        //返回
        return E3Result.ok();
    }

    /**
     * 说明     :  合并购物车
     *
     * @param userId 用户ID
     * @param list   购物车列表
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result mergeCart(long userId, List<TbItem> list) {
        for (TbItem item : list) {
            addCart(userId, item.getId(), item.getNum());
        }
        return E3Result.ok();
    }

    /**
     * 说明     :  获取购物车列表
     *
     * @param userId
     * @return :   java.util.List<com.e3.pojo.TbItem>
     */
    @Override
    public List<TbItem> getCartList(long userId) {
        List<String> list = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> result = new ArrayList<>();
        for (String item : list) {
            result.add(JsonUtils.jsonToPojo(item, TbItem.class));
        }
        return result;
    }

    /**
     * 说明     :  更新商品数量
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param num    商品数量
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result updateCart(long userId, long itemId, int num) {
        //从redis获取对应商品
        String temp = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(temp, TbItem.class);
        //更新商品数量
        item.setNum(num);
        //写回redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        //返回
        return E3Result.ok();
    }

    /**
     * 说明     :  删除商品
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result delCart(long userId, long itemId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    /**
     * 说明     :  根据用户ID和商品ID列表删除购物车
     *
     * @param userId   用户ID
     * @param itemIds   商品ID列表
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result delCartList(long userId, List<String> itemIds) {
        for (String id : itemIds) {
            jedisClient.hdel(REDIS_CART_PRE + ":" + userId, id);
        }
        return E3Result.ok();
    }

    /**
     * 说明     :  删除用户所有购物车
     *
     * @param userId 用户ID
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result clearCartItem(long userId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }

}
