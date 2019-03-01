package com.e3.search.service.impl;

import com.e3.commons.pojo.SearchItem;
import com.e3.commons.util.E3Result;
import com.e3.search.mapper.ItemMapper;
import com.e3.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
/**
 * 索引维护Service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrClient solrClient;

    @Override
    public E3Result importAllItems() {
        //查询索引
        List<SearchItem> itemList = itemMapper.getItemList();
        try {
            //存储索引
            solrClient.addBeans(itemList);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return E3Result.build(E3Result.INSERT_ERROR, "数据导入异常");
        }
        return E3Result.ok();
//        List<SolrInputDocument> docs = new LinkedList<>();
        //遍历索引
//        if (itemList != null && itemList.size() > 0) {
//            for (SearchItem item : itemList) {
//                SolrInputDocument doc = new SolrInputDocument();
//                doc.addField("id",item.getId());
//                doc.addField("item_title",item.getTitle());
//                doc.addField("item_sell_point",item.getSell_point());
//                doc.addField("item_price",item.getPrice());
//                doc.addField("item_image",item.getImage());
//                doc.addField("item_category_name",item.getCategory_name());
//                docs.add(doc);
//            }
//        }
    }
}
