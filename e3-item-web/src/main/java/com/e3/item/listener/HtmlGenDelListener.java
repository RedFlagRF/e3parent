package com.e3.item.listener;

import org.springframework.beans.factory.annotation.Value;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;

/**
 * @Author RedFlag
 * @Description  商品下架,删除静态化监听器
 * @Date 11:24 2019/2/16
 */
public class HtmlGenDelListener implements MessageListener {
    @Value(("${HTML_GEN_PATH}"))
    private String HTML_GEN_PATH;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage mes = (TextMessage) message;
            Long id = Long.valueOf(mes.getText());
            File file = new File(HTML_GEN_PATH + id + ".html");
            if (file.exists()) {
                file.delete();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
