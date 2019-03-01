package com.e3.search.service;

import com.e3.commons.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

public interface SearchService {
    /**
     * 说明     :  根据关键词分页查询商品
     *
     * @param keyword 关键词
     * @param page    当前页
     * @param rows    页大小
     * @return :  com.e3.commons.pojo.SearchResult
     */
    SearchResult search(String keyword, int page, int rows) throws IOException, SolrServerException;
}
