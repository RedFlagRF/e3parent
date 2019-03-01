package com.e3.controller;

import com.e3.commons.util.E3Result;
import com.e3.pojo.TbItemDesc;
import com.e3.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author RedFlag
 * @Description 商品描述Controller
 * @Date 13:09 2019/1/26
 */
@Controller
public class ItemDescController {
    @Autowired
    private ItemDescService itemDescService;

    /**
     * 说明     :  商品描述明细
     *
     * @param id
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public E3Result itemDesc(@PathVariable long id) {
        TbItemDesc itemDesc = itemDescService.getTbItemDesc(id);
        return E3Result.ok(itemDesc);
    }
//    @RequestMapping("/rest/item/param/item/query/${id}")
//    @ResponseBody
//    public E3Result itemQuery(@PathVariable long id) {
//        System.out.println(id);
//        return E3Result.ok();
//    }
}
