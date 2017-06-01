package com.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 *  <p>Title: PageController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年5月31日 下午1:05:24
 */
@Controller
public class PageController {
	//首页跳转
	@RequestMapping("/")
	public String showIndex() throws Exception{
		return "index";
	}
	//其他页面的跳转
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) throws Exception{
		return page;
	}
}
