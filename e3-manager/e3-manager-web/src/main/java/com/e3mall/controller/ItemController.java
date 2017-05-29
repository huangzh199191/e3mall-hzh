package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;
/**
 * 商品管理controller
 *  <p>Title: ItemController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年5月29日 下午1:05:19
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemSerivce;
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public TbItem getItemById(@PathVariable("id") Long itemId){
		TbItem tbItem =  itemSerivce.getItemById(itemId);
		return tbItem;
	}
}
