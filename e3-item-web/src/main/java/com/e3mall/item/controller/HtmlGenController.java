package com.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * 生成静态页面的controller
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//获取freemarker的Configuration对象
		Configuration configuration = freeMarkerConfig.getConfiguration();
		//创建一个模板对象。
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个模板使用的数据集
		Map data = new HashMap<>();
		//添加数据
		data.put("hello", "hello spring-freeMarker");
		//创建一个Writer对象
		Writer writer = new FileWriter(new File("E:/temp/freemarker/test.html"));
		//调用模板对象的process方法输出文件
		template.process(data, writer);
		//关闭流
		writer.close();
		return "ok";
	}
}
