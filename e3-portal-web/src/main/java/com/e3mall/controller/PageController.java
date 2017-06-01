package com.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 页面跳转的controller
 */
@Controller
public class PageController {
	
	/*
	 * 首页跳转
	 * url /index
	 * 参数 无
	 * 返回值  index
	 */
	@RequestMapping("/index")
	public String showIndex(){
		return "index";
	}
}
