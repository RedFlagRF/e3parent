package com.e3.search.dao;

import com.e3.commons.pojo.SearchItem;
import com.e3.commons.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author RedFlag
 * @Description 商品搜索Dao
 * @Date 21:54 2019/2/11
 */
@Repository
public class SearchDao {


    @Autowired
    private SolrClient solrClient;

    /**
     * 说明     :  根据条件查询索引结果
     *
     * @param query
     * @return :  com.e3.commons.pojo.SearchResult
     */
    public SearchResult search(SolrQuery query) throws IOException, SolrServerException {
       //查询
        QueryResponse response = solrClient.query(query);
        //获取beans对象
        List<SearchItem> itemList = response.getBeans(SearchItem.class);
        SearchResult result = new SearchResult();
        //获取记录数
        result.setRecordCount(response.getResults().getNumFound());
        //获取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //设置高亮结果
        for (SearchItem item : itemList) {
            List<String> item_title = highlighting.get(item.getId()).get("item_title");
            if (item_title != null && item_title.size() > 0) {
                item.setTitle(item_title.get(0));
            }
        }
        result.setItemList(itemList);
        return result;
    }
}
