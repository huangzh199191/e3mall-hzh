package com.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${index_slide_cid}")
	private Long INDEX_SLIDE_CID;
	
	@Autowired
	private ContentService contentService;
	/*
	 * 首页跳转
	 * url /index  配置了*.html拦截
	 * 参数 无
	 * 返回值  index
	 */
	@RequestMapping("/index")
	public String showIndex(Model model){
		//首页展示内容
		//TODO
		//首页轮播图
		List<TbContent> ad1List = contentService.getContentListByCategoryId(INDEX_SLIDE_CID);
		model.addAttribute("ad1List", ad1List);
		
		return "index";
	}
}
