package com.e3.controller;

import com.e3.commons.util.E3Result;
import com.e3.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author RedFlag
 * @Description 导入商品索引
 * @Date 16:10 2019/2/11
        */
@Controller
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;

    /**
     * 说明     :  导入商品索引
     *
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("index/item/import")
    @ResponseBody
    private E3Result importItemList() {
        return searchItemService.importAllItems();
    }
}
