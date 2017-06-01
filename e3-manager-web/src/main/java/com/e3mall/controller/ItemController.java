package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
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
	
	//商品列表(有分页)
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyuiDatagridResult getItemList(Integer page,Integer rows){
		EasyuiDatagridResult result = itemSerivce.getItemList(page, rows);
		return result;
	}
	/*
	 * 添加商品  
	 * url  /item/save
	 * 参数      表单数据 封装到Item对象  和商品描述  desc
	 * 返回值  E3Result  
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result result =  itemSerivce.addItem(item,desc);
		return result;
	}
	
}
