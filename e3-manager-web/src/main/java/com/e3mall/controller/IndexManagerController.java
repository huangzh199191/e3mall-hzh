package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.search.service.SearchItemService;

/**
 * 索引库维护的controller
 *  <p>Title: IndexManagerController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年6月5日 下午6:49:56
 */
@Controller
public class IndexManagerController {
    
	 @Autowired
	 private SearchItemService searchItemService;
	/*
	 * 一键将商品导入到索引库
	 * url /index/item/import
	 * 参数 无
	 * 返回值 E3Result
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemsToIndex(){
		E3Result result = searchItemService.importItemsToIndex();
		return result;
	}
}
