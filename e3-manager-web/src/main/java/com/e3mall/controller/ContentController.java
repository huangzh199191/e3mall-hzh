package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

/**
 * 内容管理controller
 *  <p>Title: ContentController</p>
 *	<p>Description: </p>
 *  <p>Company: </p>
 *	@author Administrator
 *  @date 2017年6月2日 下午5:26:19
 */

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	/*
	 * 内容列表
	 * url /content/query/list
	 * 参数  分类id categoryId  分页  rows page
	 * 返回值 EasyuiDatagridResult
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyuiDatagridResult getContentList(Long categoryId,Integer rows,Integer page){
		EasyuiDatagridResult result = contentService.getContentList(categoryId, rows, page);
		return result;
	}
	/*
	 * 增加内容
	 * url /content/save
	 * 参数  Tbcontent
	 * 返回值 E3Result
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		E3Result result = contentService.addContent(content);
		return result;
	}
	/*
	 * 修改内容
	 * url /rest/content/edit
	 * 参数  Tbcontent
	 * 返回值 E3Result
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3Result updateContent(TbContent content){
		E3Result result = contentService.updateContent(content);
		return result;
	}
	/*
	 * 删除内容
	 * url /content/delete
	 * 参数 ids
	 * 返回值 E3Result
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(Long[] ids){
		E3Result result = contentService.deleteContent(ids);
		return result;
	}
}
