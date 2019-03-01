package com.e3.item.listener;

import com.e3.item.pojo.Item;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbItemDesc;
import com.e3.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author RedFlag
 * @Description 监听商品添加, 修改消息, 生成静态页面
 * @Date 10:33 2019/2/16
 */
public class HtmlGenAddOrUpdateListener implements MessageListener {
    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        try {
            //创建模版
            //从商品取出消息
            TextMessage mes= (TextMessage) message;
            // 根据商品ID查询基本信息和商品描述
            Long id= Long.valueOf(mes.getText());
            //等待事务提交
            Thread.sleep(1000);
            TbItem itemById = itemService.getItemById(id);
            Item item = new Item(itemById);
            TbItemDesc itemDesc = itemService.getItemDescById(id);
            //创建数据集,封装商品
            Map<String, Object> data = new HashMap<>();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //加载模版对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建输出流,指定输出文件目录及名称
            Writer out = new FileWriter(HTML_GEN_PATH + id + ".html");
            //生成静态页面
            template.process(data, out);
            //关闭流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
