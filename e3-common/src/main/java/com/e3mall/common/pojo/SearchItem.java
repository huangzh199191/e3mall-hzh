package com.e3mall.common.pojo;

import java.io.Serializable;

/*
 *SELECT 
a.id,
a.title,
a.sell_point,
a.price,
a.image,
b.name item_category_name,

FROM tb_item a
LEFT JOIN tb_item_cat b ON a.cid=b.id
 */
public class SearchItem implements Serializable{
	private String id;
	private String title;
	private String sell_point;
	private Long price;
	private String image;
	private String item_category_name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getItem_category_name() {
		return item_category_name;
	}
	public void setItem_category_name(String item_category_name) {
		this.item_category_name = item_category_name;
	}
	
	
	
}
