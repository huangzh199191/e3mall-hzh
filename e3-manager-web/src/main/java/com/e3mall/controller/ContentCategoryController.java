package com.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiTreeNode;
import com.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类controller
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	/*
	 * 展示内容分类树
	 * url  /content/category/list
	 * 参数    id（父节点）
	 * 返回值  List<EasyuiTreeNode> json
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyuiTreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		List<EasyuiTreeNode> list = contentCategoryService.getContentCategoryList(parentId);
		return list;
	}
	/*
	 * 新增内容分类
	 * url /content/category/create
	 * 参数   parentId  name
	 * 返回值  E3Result   ContentCategory
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategory(Long parentId,String name){
		E3Result result = contentCategoryService.addContentCategory(parentId,name);
		return result;
	}
	/*
	 * 重命名内容分类
	 * url /content/category/update
	 * 参数 id  name
	 * 返回值 null
	 */
	@RequestMapping("/content/category/update")
	public void updateContentCategory(Long id,String name){
		contentCategoryService.updateContentCategory(id,name);
	}
	/*
	 * 删除分类
	 * url /content/category/delete/{id}
	 * 参数 id
	 * 返回值 null
	 */
	@RequestMapping("/content/category/delete")
	public void deleteContentCategory(Long id){
		contentCategoryService.deleteContentCategory(id);
	}
}
