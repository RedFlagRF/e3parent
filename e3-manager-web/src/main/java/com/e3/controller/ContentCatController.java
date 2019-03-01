package com.e3.controller;

import com.e3.commons.pojo.EasyUiTreeNode;
import com.e3.commons.util.E3Result;
import com.e3.service.ContentCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author RedFlag
 * @Description 内容分类管理Controller
 * @Date 21:24 2019/1/26
 */
@Controller
public class ContentCatController {
    @Autowired()
    private ContentCatService contentCatService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUiTreeNode> getContentCatList(@RequestParam(name = "id", defaultValue = "0") Long parentID) {
        return contentCatService.getContentCategoryByParentId(parentID);
    }

    /**
     * 说明     :  添加内容分类
     *
     * @param parentId 父ID
     * @param name     分类内容
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContentCat(Long parentId, String name) {
        return contentCatService.addContentCategory(parentId, name);
    }

    /**
     * 说明     :  更新内容分类名称
     *
     * @param id   内容分类ID
     * @param name 内容名称
     * @return :  com.e3.commons.util.E3Result
     */
    @RequestMapping("/content/category/update")
    public void updateContentCat(Long id, String name) {
        contentCatService.updateCntentCat(id, name);
    }
/**
*  说明     :  删除内容分类
  * @param      id   内容ID
* @return  :  com.e3.commons.util.E3Result
*/
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public E3Result delContentCat(Long id) {
        return contentCatService.delContentCat(id);
    }
}
