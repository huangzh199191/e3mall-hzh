package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.dao.TbItemDescMapper;
import com.e3mall.dao.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.pojo.TbItemExample.Criteria;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 商品管理service
 *  <p>Title: ItemServiceImpl</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年5月29日 下午1:04:55
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	//redis缓存相关
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;
	
	//发送消息相关
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="itemChangeTopic")
	private Destination destination;
	
	//根据商品id查询商品
	public TbItem getItemById(Long id) {
		try {
			//查询redis缓存,有就直接用
			String json = jedisClient.get(ITEM_INFO+":"+id+":BASE");
			if(StringUtils.isNotBlank(json)){
				return JsonUtils.jsonToObject(json, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		//设置查询条件
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list!=null&&list.size()>0){
			TbItem tbItem = list.get(0);
			try {
				//添加到缓存
				String json = JsonUtils.ObjectToJson(tbItem);
				jedisClient.set(ITEM_INFO+":"+id+":BASE", json);
				//设置过期时间
				jedisClient.expire(ITEM_INFO+":"+id+":BASE", ITEM_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return tbItem;
		}
		return null;
	}
	//商品列表
	public EasyuiDatagridResult getItemList(Integer page, Integer rows) {
		//设置分页
		PageHelper.startPage(page, rows); 
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取出结果
		PageInfo pageInfo = new PageInfo<>(list);
			//取出总记录数
		long total = pageInfo.getTotal();
		EasyuiDatagridResult result = new EasyuiDatagridResult();
		result.setTotal(total);
		result.setRows(list);
		//返回结果
		return result;
	}
	//添加商品
	public E3Result addItem(TbItem item, String desc) {
		//商品id
		final long itemId = IDUtils.genItemId();
		//补充item属性
		item.setId(itemId);
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		//向商品表添加数据
		itemMapper.insert(item);
		//商品描述
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		itemDesc.setItemDesc(desc);
		//向商品描述表添加数据
		itemDescMapper.insert(itemDesc);
		
		//发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		//同步redis缓存
		jedisClient.del(ITEM_INFO+":"+itemId+":BASE");
		jedisClient.del(ITEM_INFO+":"+itemId+":DESC");
		
		//返回值
		return E3Result.ok();
	}
	//删除商品
	public E3Result deleteItems(Long[] ids) {
		for (Long id : ids) {
			//根据id查询商品
			TbItem item = itemMapper.selectByPrimaryKey(id);
			item.setStatus((byte) 3);
			
			//同步redis缓存
			jedisClient.del(ITEM_INFO+":"+id+":BASE");
			jedisClient.del(ITEM_INFO+":"+id+":DESC");
			
			//更新数据库
			itemMapper.updateByPrimaryKey(item);
		}
		//返回值
		return E3Result.ok();
	}
	//下架商品
	public E3Result updateInstockItems(Long[] ids) {
		for (Long id : ids) {
			TbItem item = itemMapper.selectByPrimaryKey(id);
			item.setStatus((byte) 2);
			
			//同步redis缓存
			jedisClient.del(ITEM_INFO+":"+id+":BASE");
			jedisClient.del(ITEM_INFO+":"+id+":DESC");
			
			itemMapper.updateByPrimaryKey(item);
		}
		return E3Result.ok();
	}
	//上架商品
	public E3Result updateReshelfItems(Long[] ids) {
		for (Long id : ids) {
			TbItem item = itemMapper.selectByPrimaryKey(id);
			item.setStatus((byte) 1);
			
			//同步redis缓存
			jedisClient.del(ITEM_INFO+":"+id+":BASE");
			jedisClient.del(ITEM_INFO+":"+id+":DESC");
			
			itemMapper.updateByPrimaryKey(item);
		}
		return E3Result.ok();
	}
	//查看商品描述
	public E3Result getItemDescById(Long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
				TbItemDesc tbItemDesc = JsonUtils.jsonToObject(json, TbItemDesc.class);
				return E3Result.ok(tbItemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//添加到缓存
		try {
			String json = JsonUtils.ObjectToJson(itemDesc);
			jedisClient.set(ITEM_INFO+":"+itemId+":DESC", json);
			//过期时间
			jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok(itemDesc);
	}
	//修改商品
	public E3Result updateItem(TbItem item, String desc) {
		//根据id获得商品
		final TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
		//更改属性
		tbItem.setCid(item.getCid());
		tbItem.setTitle(item.getTitle());
		tbItem.setSellPoint(item.getSellPoint());
		tbItem.setPrice(item.getPrice());
		tbItem.setNum(item.getNum());
		tbItem.setBarcode(item.getBarcode());
		tbItem.setImage(item.getImage());
		//更改时间
		Date date = new Date();
		tbItem.setUpdated(date);
		//根据商品id获取商品描述
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(item.getId());
		//更改属性
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(date);
		//提交到数据库
		itemMapper.updateByPrimaryKey(tbItem);
		itemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);
		//itemDescMapper.updateByPrimaryKey(tbItemDesc);
		
		//发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(tbItem.getId()+"");
				return textMessage;
			}
		});
		
		//同步redis缓存
		jedisClient.del(ITEM_INFO+":"+tbItem.getId()+":BASE");
		jedisClient.del(ITEM_INFO+":"+tbItem.getId()+":DESC");
		
		return E3Result.ok();
	}
	
	
}
