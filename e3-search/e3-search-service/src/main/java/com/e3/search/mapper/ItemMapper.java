package com.e3.search.mapper;

import com.e3.commons.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {
    List<SearchItem> getItemList();
/**
*  说明     :  通过ID查询item
  * @param      itemid
* @return  :  com.e3.commons.pojo.SearchItem
*/
    SearchItem getItemById(long itemid);
}
