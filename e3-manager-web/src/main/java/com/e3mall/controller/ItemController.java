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
	/* 
	 * 删除商品
	 * url /rest/item/delete
	 * 参数  ids
	 * 返回值 E3Result
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItems(Long[] ids){
		E3Result result = itemSerivce.deleteItems(ids);
		return result;
	}
	/**
	 * 商品下架
	 * url /rest/item/instock
	 * 参数 ids
	 * 返回值 E3Result
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result updateInstockItems(Long[] ids){
		E3Result result =  itemSerivce.updateInstockItems(ids);
		return result;
	}
	/*
	 * 商品上架
	 * url /rest/item/reshelf
	 * 参数 ids
	 * 返回值 E3Result
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result updateReshelfItems(Long[] ids){
		E3Result result = itemSerivce.updateReshelfItems(ids);
		return result;
	}
	/*
	 * 查看商品描述
	 * url /rest/item/query/item/desc/{id}
	 * 参数 id
	 * 返回值 E3Result  data为 ItemDesc
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result getItemDescById(@PathVariable("id") Long itemId){
		E3Result result = itemSerivce.getItemDescById(itemId);
		return result;
	}
	/*
	 * 修改商品
	 * url /rest/item/update
	 * 参数 item  desc
	 * 返回值 E3Result
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		E3Result result = itemSerivce.updateItem(item,desc);
		return result;
	}
	
	
	
	
}
