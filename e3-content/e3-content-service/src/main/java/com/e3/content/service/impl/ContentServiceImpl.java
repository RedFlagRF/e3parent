package com.e3.content.service.impl;

import com.e3.commons.jedis.JedisClient;
import com.e3.commons.pojo.EasyUiDataGridResult;
import com.e3.commons.util.E3Result;
import com.e3.commons.util.JsonUtils;
import com.e3.mapper.TbContentMapper;
import com.e3.pojo.TbContent;
import com.e3.pojo.TbContentExample;
import com.e3.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author RedFlag
 * @Description 内容Service
 * @Date 10:44 2019/1/28
 */
@Service
public class ContentServiceImpl implements ContentService {

    private final TbContentMapper contentMapper;
    private final JedisClient jedisClient;
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Autowired
    public ContentServiceImpl(JedisClient jedisClient, TbContentMapper contentMapper) {
        this.jedisClient = jedisClient;
        this.contentMapper = contentMapper;
    }

    /**
     * 说明     :  添加内容
     *
     * @param category 内容
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result addContent(TbContent category) {
        try {
            category.setCreated(new Date());
            category.setUpdated(new Date());
            contentMapper.insert(category);
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(E3Result.INSERT_ERROR, "插入失败");
        }
        jedisClient.hdel(CONTENT_LIST, category.getCategoryId().toString());
        return E3Result.ok();
    }

    /**
     * 说明     :  内容分页查询
     *
     * @param categoryId 分类id
     * @param page       当前页
     * @param rows       每页数量
     * @return :  com.e3.commons.util.EasyUiDataGridResult
     */
    @Override
    public EasyUiDataGridResult listConTentByCategoryId(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //查询
        List<TbContent> list = contentMapper.selectByExample(example);
        //取结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUiDataGridResult result = new EasyUiDataGridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 说明     :  更新内容
     *
     * @param content
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result updateContent(TbContent content) {
        try {
            contentMapper.updateByPrimaryKeySelective(content);
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(E3Result.UPDATE_ERROR, "更新错误");
        }
        jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
        return E3Result.ok();
    }

    /**
     * 说明     :  批量删除内容
     *
     * @param ids arrays id
     * @return :  com.e3.commons.util.E3Result
     */
    @Override
    public E3Result buliDelContent(Long[] ids) {

        if (ids != null && ids.length > 0) {
            TbContent temp = contentMapper.selectByPrimaryKey(ids[0]);
            System.out.println(temp.getCategoryId());
            jedisClient.hdel(CONTENT_LIST, temp.getCategoryId().toString());
        }
        try {
            contentMapper.bulkDeleteByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(E3Result.DEL_ERROR, "删除错误");
        }

        return E3Result.ok();
    }

    /**
     * 说明     :  通过分类查询所有内容
     *
     * @param cid : 分类ID
     * @return :  java.util.List<com.e3.pojo.TbContent>
     */
    @Override
    public List<TbContent> listConTentByCid(Long cid) {
        try {
            //查询缓存
            String list = jedisClient.hget(CONTENT_LIST, String.valueOf(cid));
            if (StringUtils.isNotBlank(list)) {
                //有, 直接响应
                return JsonUtils.jsonToList(list, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //无,查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        try {
            //无,存缓存
            jedisClient.hset(CONTENT_LIST, String.valueOf(cid), JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
