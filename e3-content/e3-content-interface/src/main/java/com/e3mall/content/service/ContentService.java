package com.e3mall.content.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.pojo.TbContent;

public interface ContentService {
	
	EasyuiDatagridResult getContentList(Long categoryId,Integer rows,Integer page);

	E3Result addContent(TbContent content);

	E3Result updateContent(TbContent content);

	E3Result deleteContent(Long[] ids);

	E3Result getContentById(Long id);

}
