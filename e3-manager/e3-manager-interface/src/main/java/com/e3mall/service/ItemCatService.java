package com.e3mall.service;

import java.util.List;

import com.e3mall.common.pojo.EasyuiTreeNode;

public interface ItemCatService {
	List<EasyuiTreeNode> getItemCatList(Long parentId);
}
