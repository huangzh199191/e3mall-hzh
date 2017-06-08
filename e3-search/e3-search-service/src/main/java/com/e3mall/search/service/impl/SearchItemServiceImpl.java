package com.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.SearchItemMapper;
import com.e3mall.search.service.SearchItemService;


@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
		
	//将商品导入到索引库
	public E3Result importItemsToIndex() {
		try {
			//查询商品信息
			List<SearchItem> list = searchItemMapper.getSearchItemList();
			for (SearchItem searchItem : list) {
				//创建文档
				SolrInputDocument document = new SolrInputDocument();
				//设置业务域
				document.setField("id", searchItem.getId());
				document.setField("item_title", searchItem.getTitle());
				document.setField("item_price", searchItem.getPrice());
				document.setField("item_sell_point", searchItem.getSell_point());
				document.setField("item_image", searchItem.getImage());
				document.setField("item_category_name", searchItem.getItem_category_name());
				//添加到索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			//返回结果
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			//导入失败
			return E3Result.build(500, "商品导入索引库失败");
		}
	}

	//商品更改时（添加，修改），更新索引库
	public E3Result addSearchItemToIndex(SearchItem searchItem) throws Exception {
		//获得文档对象
		SolrInputDocument document = new SolrInputDocument();
		//创建域
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getItem_category_name());
		//把文档添加到索引库
		solrServer.add(document);
		solrServer.commit();
		return E3Result.ok();
	}

}
