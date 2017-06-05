package com.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.content.service.ContentService;
import com.e3mall.dao.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.e3mall.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	//内容列表
	public EasyuiDatagridResult getContentList(Long categoryId, Integer rows, Integer page) {
		//设置分页
		PageHelper.startPage(page, rows);
		//执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//取出结果
		PageInfo pageInfo = new PageInfo<>(list);
		//取出总记录数
		long total = pageInfo.getTotal();
		//设置返回值
		EasyuiDatagridResult result = new EasyuiDatagridResult();
		result.setRows(list);
		result.setTotal(total);
		return result;
	}
	
	
	//添加内容
	public E3Result addContent(TbContent content) {
		//id是自增的
		//补全属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//提交到数据库
		contentMapper.insert(content);
		//缓存同步
		jedisClient.hdel("CONTENT_INFO", content.getCategoryId().toString());
		//设置返回值
		return E3Result.ok();
	}
	//修改内容
	public E3Result updateContent(TbContent content) {
		//根据id查询TbContent对象
		TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
		//修改以及补全属性
		tbContent.setTitle(content.getTitle());
		tbContent.setSubTitle(content.getSubTitle());
		tbContent.setTitleDesc(content.getTitleDesc());
		tbContent.setUrl(content.getUrl());
		tbContent.setPic(content.getPic());
		tbContent.setPic2(content.getPic2());
		tbContent.setContent(content.getContent());
		
		tbContent.setUpdated(new Date());
		//提交数据库
		contentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
		//缓存同步
		jedisClient.hdel("CONTENT_INFO", tbContent.getCategoryId().toString());
		//设置返回值
		return E3Result.ok();
	}
	//删除内容
	public E3Result deleteContent(Long[] ids) {
		for (Long id : ids) {
			
			//缓存同步
			TbContent content = contentMapper.selectByPrimaryKey(id);
			jedisClient.hdel("CONTENT_INFO", content.getCategoryId().toString());
			
			contentMapper.deleteByPrimaryKey(id);
		}
		return E3Result.ok();
	}
	
	//根据id查询内容
	public E3Result getContentById(Long id) {
		TbContent content = contentMapper.selectByPrimaryKey(id);
		return E3Result.ok(content);
	}
	
	//通过分类id查询内容
	public List<TbContent> getContentListByCategoryId(Long categoryId) {
		try {
			//先查询缓存
			String json = jedisClient.hget("CONTENT_INFO", categoryId+"");
			//有缓存，直接响应回去
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToObjectList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//没有缓存，查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//添加到缓存
		try {
			String json = JsonUtils.ObjectToJson(list);
			jedisClient.hset("CONTENT_INFO", categoryId+"", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
