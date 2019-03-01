package com.e3.content.service.impl;

import com.e3.commons.pojo.EasyUiTreeNode;
import com.e3.commons.util.E3Result;
import com.e3.mapper.TbContentCategoryMapper;
import com.e3.pojo.TbContentCategory;
import com.e3.pojo.TbContentCategoryExample;
import com.e3.service.ContentCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author RedFlag
 * @Description 内容分类管理Service
 * @Date 21:15 2019/1/26
 */
@Service
public class ContentCatServiceImpl implements ContentCatService {


    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 说明     :  通过父id查询目录
     *
     * @param parentId 父id
     * @return :  java.util.List<com.e3.commons.pojo.EasyUiTreeNode>
     */
    @Override
    public List<EasyUiTreeNode> getContentCategoryByParentId(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> li = contentCategoryMapper.selectByExample(example);
        List<EasyUiTreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : li) {
            EasyUiTreeNode node = new EasyUiTreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getIsParent() ? "closed" : "open");
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 说明     :  添加目录分类
     *
     * @param parentId
     * @param name
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result addContentCategory(long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();  //设置TbContentCategory pojo
        //设置属性
        contentCategory.setIsParent(false);
        contentCategory.setName(name);
        contentCategory.setSortOrder(1);//1 默认排序
        contentCategory.setStatus(1);// 1正常状态
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategory.setParentId(parentId);
        contentCategoryMapper.insert(contentCategory);
        //查询parent
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        //查看是否为父节点
        if (!parent.getIsParent()) {
            //不是, 更新为父节点
            parent.setIsParent(true);
            parent.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
      /*  更新二
      TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIsParentEqualTo(false).andIdEqualTo(parentId);
        TbContentCategory parent=new TbContentCategory();
        parent.setIsParent(true);
        contentCategoryMapper.updateByExampleSelective(parent, example);*/
        return E3Result.ok(contentCategory);
    }

    /**
     * 说明     :  更新内容分类名称
     *
     * @param id   内容分类ID
     * @param name 内容名称
     */
    @Override
    public void updateCntentCat(Long id, String name) {
        //获取pojo
        TbContentCategory cc = new TbContentCategory();
        //设置pojo属性
        cc.setId(id);
        cc.setName(name);
        cc.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(cc);
    }

    /**
     * 说明     :  删除内容分类
     *
     * @param id 内容ID
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result delContentCat(Long id) {
        //获取当前对象
        TbContentCategory current = contentCategoryMapper.selectByPrimaryKey(id);
//        long count = contentCategoryMapper.countByExample(example);
        //判断是否为父节点
        if (current.getIsParent()) {
            //父节点,拒绝删除
            return E3Result.build(E3Result.OTHER_ERROR, "该节点为父节点,请删除子节点后删除父节点");
        }
        //获得父节点ID
        long parentId = current.getParentId();
        if (parentId == 0) { //判断是否根节点
            //根节点,拒绝删除
            return E3Result.build(E3Result.OTHER_ERROR, "该节点为根节点,只能修改,不能删除");
        }
        //获取example对象
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询对象
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);// 正常状态内容分类
        criteria.andParentIdEqualTo(parentId);
        //查询,获取父节点子节点数量
        long count = contentCategoryMapper.countByExample(example);
        /* 父节点只有当前要删除叶子节点 */
        if (count == 1) {
            //更新父节点
            TbContentCategory contentCategory = new TbContentCategory();
            contentCategory.setId(parentId);
            contentCategory.setIsParent(false);
            contentCategory.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        //删除当前节点,更新状态
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setId(id);
        //1 正常, 2 删除状态
        contentCategory.setStatus(2);
        contentCategory.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return E3Result.ok();
    }
}
