package com.e3.service;

import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbItemDesc;

public interface ItemService {
    /**
     * 说明     :  根据ID查询商品
     *
     * @param itemId
     * @return :  com.e3.pojo.TbItem
     */
    TbItem getItemById(Long itemId);

    /**
     * 说明     :  根据商品ID获取商品描述
     *
     * @param itemid 商品id
     * @return :  com.e3.pojo.TbItemDesc
     */
    TbItemDesc getItemDescById(Long itemid);

    /**
     * easyui分页查询全部
     */
    EasyUiDataGridResult getItemList(int page, int rows);

    /**
     * 说明     :  (商品状态改变)
     * * @param      id    商品ID
     * *@parem state 商品状态
     *
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result updateState(Long id, int state);

    /**
     * 说明     :  添加商品
     * * @param      item
     *
     * @param desc 商品描述
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result addItem(TbItem item, String desc);

    /**
     * 说明     :  更新商品
     *
     * @param item 商品信息
     * @param desc 商品描述
     * @return :  E3Result
     */
    E3Result updateItem(TbItem item, String desc);


//    /**
//     *  说明     :   下架商品,更新商品状态
//     * @param      id    商品ID
//     * @return  :  com.e3.commons.util.E3Result
//     */
//    E3Result  undercarriageItem(Long id);
//    /**
//     *  说明     :  删除商品 ,更新商品状态
//     * @param      id    商品ID
//     * @return  :  com.e3.commons.util.E3Result
//     */

//    E3Result  delItem(Long id);
//    /**
//     *  说明     :  重新上架,更新商品状态
//     * @param      id    商品ID
//     * @return  :  com.e3.commons.util.E3Result
//     */
//    E3Result  reshelfItem(Long id);
}
