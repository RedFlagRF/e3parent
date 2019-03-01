package com.e3.service.impl;

import com.e3.commons.jedis.JedisClient;
import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.IDUtils;
import com.e3.commons.util.JsonUtils;
import com.e3.mapper.TbItemDescMapper;
import com.e3.mapper.TbItemMapper;
import com.e3.pojo.TbItem;
import com.e3.pojo.TbItemDesc;
import com.e3.pojo.TbItemExample;
import com.e3.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final TbItemMapper itemMapper;
    private final TbItemDescMapper itemDescMapper;
    private final JmsTemplate jmsTemplate;
    private final Destination topicAddOrUpdateDestination;
    private final ActiveMQTopic topicDelDestination;
    private final JedisClient jedisClient;
    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Autowired
    public ItemServiceImpl(TbItemMapper itemMapper, TbItemDescMapper itemDescMapper, JmsTemplate jmsTemplate, @Qualifier("topicDelDestination") ActiveMQTopic topicDelDestination, @Qualifier("topicAddOrUpdateDestination") Destination topicAddOrUpdateDestination, JedisClient jedisClient) {
        this.itemMapper = itemMapper;
        this.itemDescMapper = itemDescMapper;
        this.jmsTemplate = jmsTemplate;
        this.topicDelDestination = topicDelDestination;
        this.topicAddOrUpdateDestination = topicAddOrUpdateDestination;
        this.jedisClient = jedisClient;
    }

    /**
     * 说明     :  根据ID查询商品
     *
     * @param itemId 商品ID
     * @return :  com.e3.pojo.TbItem
     */
    @Override
    public TbItem getItemById(Long itemId) {
        //查询redis缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有,查询数据库
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        try {
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return item;
    }

    /**
     * 说明     :  根据商品ID获取商品描述
     *
     * @param itemId 商品id
     * @return :  com.e3.pojo.TbItemDesc
     */
    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //查询redis缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有,查询数据库
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }

    /**
     * easyui分页查询全部
     *
     * @param page
     * @param rows
     */
    @Override
    public EasyUiDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //创建返回对象
        EasyUiDataGridResult result = new EasyUiDataGridResult();
        //存取结果
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    /**
     * 说明     :  添加商品
     * * @param      item
     *
     * @param item Tbitem
     * @param desc 商品描述
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result addItem(TbItem item, String desc) {

        //补全属性
        final long id = IDUtils.genItemId();
        item.setId(id);  //生成商品ID
        item.setCreated(new Date());
        item.setUpdated(new Date());
        item.setStatus((byte) 1);
        itemMapper.insert(item);        //商品表插入
        TbItemDesc itemDesc = new TbItemDesc();//商品描述插pojo
        itemDesc.setItemId(item.getId());//商品描述插入数据
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
        //发送商品添加消息
        sendAddOrUpdateMessage(id, topicAddOrUpdateDestination);
        //返回结果
        return E3Result.ok();
    }

    /**
     * 说明     :  更新商品
     *
     * @param item 商品信息
     * @param desc 商品描述
     * @return :  E3Result
     */
    @Override
    public E3Result updateItem(TbItem item, String desc) {
        try {
            //更新商品
            item.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(item);
            //更新商品描述
            TbItemDesc temp = new TbItemDesc();
            temp.setItemId(item.getId());
            temp.setUpdated(new Date());
            temp.setItemDesc(desc);
            itemDescMapper.updateByPrimaryKeySelective(temp);
            //删除redis 该商品缓存
            jedisClient.expire(REDIS_ITEM_PRE + ":" + item.getId() + ":BASE", 0);
            jedisClient.expire(REDIS_ITEM_PRE + ":" + item.getId() + ":DESC", 0);
            //发送商品添加消息
            sendAddOrUpdateMessage(item.getId(), topicAddOrUpdateDestination);
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(E3Result.UPDATE_ERROR, "更新错误");
        }
        return E3Result.ok();
    }


    /**
     * 说明     :  (商品状态改变)
     * * @param      id    商品ID
     * *@parem state 商品状态
     *
     * @param id
     * @param state
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result updateState(Long id, int state) {
        //构建更新对象
        TbItem temp = new TbItem();
        //设置条件
        temp.setUpdated(new Date());
        temp.setId(id);
        temp.setStatus((byte) state);
        itemMapper.updateByPrimaryKeySelective(temp);
        //删除redis 该商品缓存
        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", 0);
        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":DESC", 0);
        //发送商品添加消息
        if (1 == state) {//上架 更新索引
            sendAddOrUpdateMessage(id, topicAddOrUpdateDestination);
        } else {
            //下架,删除商品, 删除索引
            sendDelMessage(id, topicDelDestination);
        }
        return E3Result.ok();
    }

    /**
     * 说明     :  发送商品添加或修改信息
     *
     * @param id          商品id
     * @param destination 目的地
     * @return :  void
     */
    private void sendAddOrUpdateMessage(final long id, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(String.valueOf(id));
            }
        });
    }

    /**
     * 说明     :  发送商品删除商品索引
     *
     * @param id          商品id
     * @param destination 目的地
     * @return :  void
     */
    private void sendDelMessage(final long id, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(String.valueOf(id));
            }
        });
    }


    //    /**
    //     * 说明     :  删除商品 ,更新商品状态
    //     *
    //     * @param id 商品ID
    //     * @return :  com.e3.commons.util.E3Result
    //     */
  /*  @Override
    public E3Result delItem(Long id) {
        //构建更新对象
        TbItem temp = new TbItem();
        //设置条件
        temp.setUpdated(new Date());
        temp.setId(id);
        temp.setStatus((byte) 3);
        itemMapper.updateByPrimaryKeySelective(temp);
        //删除redis 该商品缓存
        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", 0);
        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":DESC", 0);
        //删除商品, 删除索引
        sendDelMessage(id, topicDelDestination);
        return E3Result.ok();
    }*/
//    /**
//     * 说明     :  重新上架,更新商品状态
//     *
//     * @param id 商品ID
//     * @return :  com.e3.commons.util.E3Result
//     */
//    @Override
//    public E3Result reshelfItem(Long id) {
//        //构建更新对象
//        TbItem temp = new TbItem();
//        //设置条件
//        temp.setUpdated(new Date());
//        temp.setId(id);
//        temp.setStatus((byte) 1);
//        itemMapper.updateByPrimaryKeySelective(temp);
//        //删除redis 该商品缓存
//        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", 0);
//        jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":DESC", 0);
//        //发送商品添加消息
//        //上架 更新索引
//        sendAddOrUpdateMessage(id, topicAddOrUpdateDestination);
//        return E3Result.ok();
//    }
//    /**
//     * 说明     :   下架商品,更新商品状态
//     *
//     * @param id 商品ID
//     * @return :  com.e3.commons.util.E3Result
//     */
//    @Override
//    public E3Result undercarriageItem(Long id) {
//        //构建更新对象
//        TbItem temp = new TbItem();
//        //设置条件
//        temp.setUpdated(new Date());
//        temp.setId(id);
//        temp.setStatus((byte) 2);
//        itemMapper.updateByPrimaryKeySelective(temp);
//        //删除redis 该商品缓存
//        jedisClient.expire(REDIS_ITEM_PRE+":" + id + ":BASE", 0);
//        jedisClient.expire(REDIS_ITEM_PRE+":" + id + ":DESC", 0);
//        //下架, 删除索引
//        sendDelMessage(id, topicDelDestination);
//        return E3Result.ok();
//    }
}
