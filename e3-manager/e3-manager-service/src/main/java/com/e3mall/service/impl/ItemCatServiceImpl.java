package com.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.EasyuiTreeNode;
import com.e3mall.dao.TbItemCatMapper;
import com.e3mall.pojo.TbItemCat;
import com.e3mall.pojo.TbItemCatExample;
import com.e3mall.pojo.TbItemCatExample.Criteria;
import com.e3mall.service.ItemCatService;
/*
 * 商品类目service
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	//商品类目列表
	public List<EasyuiTreeNode> getItemCatList(Long parentId) {
		//根据父节点id查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> ItemCatlist = itemCatMapper.selectByExample(example);
		//返回结果
		List<EasyuiTreeNode> EasyuiTreeNodeList = new ArrayList<>();
		for (TbItemCat itemCat : ItemCatlist) {
			EasyuiTreeNode easyuiTreeNode = new EasyuiTreeNode();
			easyuiTreeNode.setId(itemCat.getId());
			easyuiTreeNode.setText(itemCat.getName());
			easyuiTreeNode.setState(itemCat.getIsParent()?"closed":"open");
			//添加到列表
			EasyuiTreeNodeList.add(easyuiTreeNode);
		}
		return EasyuiTreeNodeList;
	}

}
