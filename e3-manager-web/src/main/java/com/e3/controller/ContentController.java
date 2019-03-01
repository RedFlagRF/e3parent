package com.e3.controller;

import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.pojo.TbContent;
import com.e3.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author RedFlag
 * @Description 内容Controller
 * @Date 10:56 2019/1/28
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUiDataGridResult listContent(Long categoryId, Integer page, Integer rows) {
        return contentService.listConTentByCategoryId(categoryId,page,rows);
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result addContent(TbContent content) {
        return contentService.addContent(content);
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public E3Result updateContent(TbContent content) {
        return contentService.updateContent(content);
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public E3Result delContent(Long[] ids) {
        return contentService.buliDelContent(ids);
    }

}
