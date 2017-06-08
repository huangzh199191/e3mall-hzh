package com.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.service.SearchService;

/**
 * 搜索的controller
 *  <p>Title: SearchController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年6月6日 上午10:38:35
 */
@Controller
public class SearchController {
	
	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	@Autowired
	private SearchService searchService;
	/*
	 * 搜索功能
	 * url  /search
	 * 参数   keyword   page(页面没有传入，默认提供) 
	 * 返回值  结果封装到model  逻辑视图
	 */
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		
		//处理get请求乱码
		keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");

		//异常测试
		//int i = 1/0;
		
		SearchResult result = searchService.search(keyword, page, PAGE_ROWS);
		
		//数据回显
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("recourdCount", result.getRecourdCount());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
}
