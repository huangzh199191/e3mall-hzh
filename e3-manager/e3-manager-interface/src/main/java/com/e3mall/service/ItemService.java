package com.e3mall.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.pojo.TbItem;

public interface ItemService {

	TbItem getItemById(Long id);
	EasyuiDatagridResult getItemList(Integer page,Integer rows);
	E3Result addItem(TbItem item, String desc);
}
