package com.e3.service.impl;

import com.e3.commons.pojo.EasyUiTreeNode;
import com.e3.mapper.TbItemCatMapper;
import com.e3.pojo.TbItemCat;
import com.e3.pojo.TbItemCatExample;
import com.e3.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author RedFlag
 * @Description 商品分类管理
 * @Date 16:49 2019/1/24
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUiTreeNode> getItemCatList(long parentId) {
        //根据parentid查询子节点
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //创建一个返回结果
        List<EasyUiTreeNode> resultList = new ArrayList<>();
        for (TbItemCat itemCat : list) {
            EasyUiTreeNode node = new EasyUiTreeNode();
            //设置属性
            node.setId(itemCat.getId());
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent() ? "closed" : "open");
            resultList.add(node);
        }
        return resultList;
    }
}
