package com.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

/*
 * 页面跳转的controller
 */
@Controller
public class PageController {
	
	@Autowired
	private ContentService contentService;
	/*
	 * 首页跳转
	 * url /index  配置了*.html拦截
	 * 参数 无
	 * 返回值  index
	 */
	@RequestMapping("/index")
	public String showIndex(@RequestParam(defaultValue="89")Long categoryId,Model model){
		
		//首页轮播图
		//TODO
		List<TbContent> ad1List = contentService.getContentListByCategoryId(categoryId);
		model.addAttribute("ad1List", ad1List);
		
		return "index";
	}
}
