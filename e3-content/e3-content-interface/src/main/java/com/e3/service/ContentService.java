package com.e3.service;

import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 说明     :  添加内容
     *
     * @param category 内容
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result addContent(TbContent category);

    /**
     * 说明     :  内容分页查询
     *
     * @param categoryId 分类id
     * @param page       当前页
     * @param rows       每页数量
     * @return :  com.e3.commons.util.EasyUiDataGridResult
     */
    EasyUiDataGridResult listConTentByCategoryId(Long categoryId, Integer page, Integer rows);

    /**
     * 说明     :  更新内容
     *
     * @param content
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result updateContent(TbContent content);

    /**
     * 说明     :  批量删除内容
     *
     * @param ids arrays id
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result buliDelContent(Long[] ids);

    /**
     * 说明     :  通过分类查询所有内容
     *
     * @param cid : 分类ID
     * @return :  java.util.List<com.e3.pojo.TbContent>
     */
    List<TbContent> listConTentByCid(Long cid);
}
