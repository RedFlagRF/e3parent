package com.e3.commons.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private long recordCount;//记录总数
    private int totalPages;//总页数
    private List<SearchItem> itemList;//查询结果

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
