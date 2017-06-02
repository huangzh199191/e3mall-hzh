package com.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiTreeNode;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.dao.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import com.e3mall.pojo.TbContentCategoryExample.Criteria;

/*
 * 内容分类service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	//内容分类列表树
	public List<EasyuiTreeNode> getContentCategoryList(Long parentId) {
		//根据父节点查询子节点的内容分类
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
		//返回结果
		List<EasyuiTreeNode> easyuiTreeNodesList = new ArrayList<>();
		for (TbContentCategory contentCategory : contentCategoryList) {
			EasyuiTreeNode easyuiTreeNode = new EasyuiTreeNode();
			easyuiTreeNode.setId(contentCategory.getId());
			easyuiTreeNode.setText(contentCategory.getName());
			easyuiTreeNode.setState(contentCategory.getIsParent()?"closed":"open");
			easyuiTreeNodesList.add(easyuiTreeNode);
		}
		
		return easyuiTreeNodesList;
	}
	//添加内容分类
	public E3Result addContentCategory(Long parentId, String name) {
		//设置ContentCategory属性
		TbContentCategory contentCategory = new TbContentCategory();
		//设置了主键返回到id
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		//该类目是否为父类目，1为true，0为false
		contentCategory.setIsParent(false);
		Date date = new Date();
		contentCategory.setCreated(date);
		contentCategory.setUpdated(date);
		//提交到数据库
		contentCategoryMapper.insert(contentCategory);
		//判断父节点isParent是否为true,如果不是就设置为true
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentNode.getIsParent()){
			parentNode.setIsParent(true);
			//更新父节点到数据库
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		//设置返回值
		return E3Result.ok(contentCategory);
	}
	//内容分类的更新
	public void updateContentCategory(Long id, String name) {
		//根据id查询
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);	
		//更新属性
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		//提交到数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
	}
	//根据id删除内容分类
	public void deleteContentCategory(Long id) {
		//根据id查询内容分类
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//根据id删除该分类
		contentCategoryMapper.deleteByPrimaryKey(id);
		//判断该分类的父节点是否还有子节点，没有就设置父节点的isParent为false
		//1）获取该分类的父节点
		Long parentId = contentCategory.getParentId();
		//2）通过父节点查询所有的子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list.size()==0){
			//3)没有子节点，将isParent设置为false
			TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
			parentNode.setIsParent(false);
			parentNode.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		//判断该分类是是否为父节点，是就级联删除子节点（递归）
		if(contentCategory.getIsParent()){
			this.deleteSonsByParentId(contentCategory.getId());
		}
	}
	//根据父节点删除所有子节点的方法
	public void deleteSonsByParentId(Long parentId){
		//根据该节点的id查询该节点的所有子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list.size()>0){
			//遍历所有的子节点
			for (TbContentCategory contentCategory : list) {
				//判断子节点是否为父节点
				//是父节点
				if(contentCategory.getIsParent()){
					//删除该节点
					contentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
					//递归调用
					this.deleteSonsByParentId(contentCategory.getId());
				}else{
					//不是父节点
					//删除该节点
					contentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
				}
			}
		}
	
	}

}
