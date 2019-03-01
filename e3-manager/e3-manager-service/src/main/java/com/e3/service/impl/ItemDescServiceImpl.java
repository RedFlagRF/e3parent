package com.e3.service.impl;

import com.e3.mapper.TbItemDescMapper;
import com.e3.pojo.TbItemDesc;
import com.e3.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Autowired
    private TbItemDescMapper itemDescMapper;

    /**
     * 说明     :  根据ID获得商品描述
     *
     * @param id 商品ID
     * @return :  com.e3.pojo.TbItemDesc
     */
    @Override
    public TbItemDesc getTbItemDesc(long id) {
        return itemDescMapper.selectByPrimaryKey(id);
    }
}
