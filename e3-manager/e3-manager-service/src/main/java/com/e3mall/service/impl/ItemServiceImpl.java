package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.dao.TbItemDescMapper;
import com.e3mall.dao.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemExample;
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
	
	@Override
	public TbItem getItemById(Long id) {
		TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		return tbItem;
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
		long itemId = IDUtils.genItemId();
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
		//返回值
		return E3Result.ok();
	}
	
	
}
