package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiTreeNode;

public interface ContentCategoryService {
	List<EasyuiTreeNode> getContentCategoryList(Long parentId);

	E3Result addContentCategory(Long parentId, String name);

	void updateContentCategory(Long id, String name);

	void deleteContentCategory(Long id);
}
