package com.e3.service;

import com.e3.commons.util.E3Result;
import com.e3.pojo.TbItemDesc;

public interface ItemDescService {
    /**
    *  说明     :  根据ID获得商品描述
      * @param      id   商品ID
    * @return  :  com.e3.pojo.TbItemDesc
    */
    TbItemDesc getTbItemDesc(long id);
}
