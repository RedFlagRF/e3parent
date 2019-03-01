package com.e3.controller;

import com.e3.commons.pojo.EasyUiTreeNode;
import com.e3.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author RedFlag
 * @Description  商品分类管理Controller
 * @Date 17:09 2019/1/24
 */
@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUiTreeNode> getList(@RequestParam(name = "id",defaultValue = "0")Long parentId) {
        return itemCatService.getItemCatList(parentId);
    }
}