package com.e3.service;

import com.e3.commons.pojo.EasyUiTreeNode;

import java.util.List;

/**
 * @Author RedFlag
 * @Description 购物车服务
 * @Date 16:48 2019/1/24
 */
public interface ItemCatService {
    List<EasyUiTreeNode> getItemCatList(long parentId);
}
