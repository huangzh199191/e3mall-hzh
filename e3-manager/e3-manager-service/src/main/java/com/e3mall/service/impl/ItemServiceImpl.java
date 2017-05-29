package com.e3mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.dao.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;
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
	@Override
	public TbItem getItemById(Long id) {
		TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		return tbItem;
	}
	
	
}
