package com.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;

/*
 * 展示商品详情页面的controller
 */
@Controller
public class ItemInfoController {
	
	@Autowired
	private ItemService itemService;
	
	/*
	 * 展示商品详情页面
	 * url /item/${item.id}
	 * 参数  商品id
	 * 返回值  包装后的item对象，tbItemDesc
	 */
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		//根据商品id查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		//根据商品id查询商品描述
		E3Result e3Result = itemService.getItemDescById(itemId);
		TbItemDesc tbItemDesc = (TbItemDesc) e3Result.getData();
		//数据回显
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		//返回页面
		return "item";
	}
}
