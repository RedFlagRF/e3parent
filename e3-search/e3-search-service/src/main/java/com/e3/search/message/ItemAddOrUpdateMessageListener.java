package com.e3.search.message;

import com.e3.commons.pojo.SearchItem;
import com.e3.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author RedFlag
 * @Description 监听商品添加信息, 接受消息后将对应商品信息同步索引库
 * @Date 9:47 2019/2/14
 */
public class ItemAddOrUpdateMessageListener implements MessageListener {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage mes = (TextMessage) message;
            Long id = Long.valueOf(mes.getText());
            //等待商品添加,修改事务提交
            Thread.sleep(1000);
            SearchItem item = itemMapper.getItemById(id);
            solrClient.addBean(item);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
