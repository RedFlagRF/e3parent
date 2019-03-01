package com.e3.controller;

import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.pojo.TbItem;
import com.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUiDataGridResult getItemList(Integer page, Integer rows) {
        return itemService.getItemList(page, rows);
    }

    /**
     * 说明     :  添加商品
     * * @param      item  商品信息
     *
     * @param desc 描述
     * @return :  com.e3.commons.util.E3Result json
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item, String desc) {
        return itemService.addItem(item, desc);
    }



    /**
     * 说明     :  删除商品
     *
     * @param ids 商品ID
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result delete(long[] ids) {
        //设置商品状态删除
        for (long id : ids) {
            itemService.updateState(id, 3);
        }
        return E3Result.ok();
    }

    /**
     * 说明     :  上架商品
     *
     * @param ids 商品ID
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public E3Result reShelf(long[] ids) {
        //设置商品状态删除
        for (long id : ids) {
            itemService.updateState(id, 1);
        }
        return E3Result.ok();
    }

    /**
     * 说明     :  下架商品
     *
     * @param ids 商品ID
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public E3Result inStock(long[] ids) {
        //设置商品状态删除
        for (long id : ids) {
            itemService.updateState(id, 2);
        }
        return E3Result.ok();
    }

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public E3Result updateItem(TbItem item,String desc) {
//        itemServic

        return itemService.updateItem(item, desc);
    }

}
