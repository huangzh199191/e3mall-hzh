package com.e3mall.item.pojo;

import org.apache.commons.lang3.StringUtils;

import com.e3mall.pojo.TbItem;

public class Item extends TbItem {

	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}
	
	public String[] getImages(){
		if(StringUtils.isNotBlank(this.getImage())){
			String[] images = this.getImage().split(",");
			return images;
		}
		return null;
	}
	
}
