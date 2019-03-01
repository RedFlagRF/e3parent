package com.e3.sso.service;

import com.e3.commons.util.E3Result;
import com.e3.pojo.TbUser;

public interface RegisterService {
    /**
     * 说明     :  根据type类型查询数据是否存在
     *
     * @param param 被查询数据
     * @param type  数据类型识别码
     * @return :  com.e3.commons.util.E3Result
     */
    E3Result checkData(String param, int type);
/**
*  说明     :  用户注册
  * @param      user 用户信息
* @return  :  com.e3.commons.util.E3Result
*/
    E3Result userRegister(TbUser user);
}
