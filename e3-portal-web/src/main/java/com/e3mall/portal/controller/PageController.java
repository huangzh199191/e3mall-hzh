package com.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 页面跳转的controller
 */
@Controller
public class PageController {
	
	/*
	 * 首页跳转
	 * url /
	 * 参数 无
	 * 返回值  index
	 */
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
}
