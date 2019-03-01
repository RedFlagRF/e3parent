package com.e3.cart.service;

import com.e3.commons.util.E3Result;
import com.e3.pojo.TbItem;

import java.util.List;

public interface CartService {
    /**
     * 说明     :  根据userid 在redis 存入购物车
     *
     * @param userId 用户id
     * @param itemId 商品ID
     * @param num    商品数量
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result addCart(long userId, long itemId, int num);

    /**
     * 说明     :  合并购物车
     *
     * @param userId 用户ID
     * @param list   购物车列表
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result mergeCart(long userId, List<TbItem> list);

    /**
     * 说明     :   获取购物车列表
     *
     * @param userId 用户id
     * @return :  java.util.List<com.e3.pojo.TbItem>
     */
    List<TbItem> getCartList(long userId);

    /**
     * 说明     :  更新商品数量
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param num    商品数量
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result updateCart(long userId, long itemId, int num);

    /**
     * 说明     :  删除商品
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result delCart(long userId, long itemId);
/**
*  说明     :  根据用户ID和商品ID列表删除购物车
  * @param      userId    用户ID
* @param      itemIds      商品ID列表
* @return  :  com.e3.commons.util.E3Result
*/
    E3Result delCartList(long userId, List<String > itemIds);
    /**
     * 说明     :  删除用户所有购物车
     *
     * @param userId 用户ID
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result clearCartItem(long userId);
}
