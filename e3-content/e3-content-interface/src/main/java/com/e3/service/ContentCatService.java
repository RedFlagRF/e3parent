package com.e3.service;

import com.e3.commons.pojo.EasyUiTreeNode;
import com.e3.commons.util.E3Result;

import java.util.List;

public interface ContentCatService {
    /**
     * 说明     :  通过父id查询目录
     *
     * @param parentId 父id
     * @return :  java.util.List<com.e3.commons.pojo.EasyUiTreeNode>
     */
    List<EasyUiTreeNode> getContentCategoryByParentId(long parentId);
/**
*  说明     :  添加目录分类
  * @param      parentId  
* @param      name   
* @return  :  com.e3.commons.util.E3Result
*/
    E3Result addContentCategory(long parentId, String name);
    /**
     *  说明     :  更新内容分类名称
     * @param      id  内容分类ID
     * @param      name   内容名称
     */
    void updateCntentCat(Long id, String name);
    /**
     *  说明     :  删除内容分类
     * @param      id   内容ID
     * @return  :  com.e3.commons.util.E3Result
     */
    E3Result delContentCat(Long id);
}
