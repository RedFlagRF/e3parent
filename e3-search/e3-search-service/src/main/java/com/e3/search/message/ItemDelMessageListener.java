package com.e3.search.message;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class ItemDelMessageListener implements MessageListener {
    @Resource
    private SolrClient solrClient;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage mes = (TextMessage) message;
            String id = mes.getText();
            solrClient.deleteById(id);
            solrClient.commit();
        } catch (JMSException | SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}
