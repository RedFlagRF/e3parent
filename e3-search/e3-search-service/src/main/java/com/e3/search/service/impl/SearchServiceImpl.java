package com.e3.search.service.impl;

import com.e3.commons.pojo.SearchResult;
import com.e3.search.dao.SearchDao;
import com.e3.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    /**
     * 说明     :  根据关键词分页查询商品
     *
     * @param keyword 关键词
     * @param page    当前页
     * @param rows    页大小
     * @return :  com.e3.commons.pojo.SearchResult
     */
    @Override
    public SearchResult search(String keyword, int page, int rows) throws IOException, SolrServerException {
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        //设置分页条件
        if (page <= 0) page = 1;
        query.setStart((page - 1) * rows);
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", "item_title");
        //设置高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style='color:red'>");
        query.setHighlightSimplePost("</em>");
        //查询
        SearchResult result = searchDao.search(query);
        //计算总页数
        long num =  (result.getRecordCount() + rows-1)/rows;
        //添加返回结果
        result.setTotalPages((int) num);
        //返回结果
        return result;
    }
}
