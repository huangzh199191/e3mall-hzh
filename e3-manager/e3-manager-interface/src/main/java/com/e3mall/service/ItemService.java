package com.e3mall.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyuiDatagridResult;
import com.e3mall.pojo.TbItem;

public interface ItemService {

	TbItem getItemById(Long id);
	EasyuiDatagridResult getItemList(Integer page,Integer rows);
	E3Result addItem(TbItem item, String desc);
	E3Result deleteItems(Long[] ids);
	E3Result updateInstockItems(Long[] ids);
	E3Result updateReshelfItems(Long[] ids);
	E3Result getItemDescById(Long itemId);
	E3Result updateItem(TbItem item, String desc);
}
