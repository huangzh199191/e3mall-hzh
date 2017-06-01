package com.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.EasyuiTreeNode;
import com.e3mall.service.ItemCatService;

/**
 * 商品类目controller
 *  <p>Title: ItemCatController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年5月31日 下午3:43:24
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	//商品类目列表
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyuiTreeNode> getItemCatList(@RequestParam(defaultValue="0",name="id")Long parentId){
		List<EasyuiTreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}
}
