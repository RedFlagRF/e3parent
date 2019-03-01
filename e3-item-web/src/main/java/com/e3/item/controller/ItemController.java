package com.e3.item.controller;

import com.e3.item.pojo.Item;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbItemDesc;
import com.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author RedFlag
 * @Description 商品详情Controller
 * @Date 10:46 2019/2/15
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        //取商品基本信息
        TbItem itemById = itemService.getItemById(itemId);
        Item item = new Item(itemById);
        //取商品描述
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //传递信息
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        //返回视图
        return "item";
    }
}
