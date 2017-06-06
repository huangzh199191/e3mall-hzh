package com.e3mall.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;

@Repository
public class SearchDaoImpl implements SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	//根据查询条件查询商品信息
	public SearchResult search(SolrQuery query) throws Exception {
		//执行查询
		QueryResponse response = solrServer.query(query);
		//取查询结果
		SolrDocumentList documentList = response.getResults();
		//总记录数
		long recourdCount = documentList.getNumFound();
		List<SearchItem> searchItemList = new ArrayList<>();
		//遍历查询的结果
		for (SolrDocument document : documentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) document.get("id"));
			//searchItem.setImage((String) document.get("item_image"));
			String item_image = (String) document.get("item_image");
			if(StringUtils.isNotBlank(item_image)){
				item_image = item_image.split(",")[0];
			}
			searchItem.setImage(item_image);
			searchItem.setItem_category_name((String) document.get("item_category_name"));
			searchItem.setPrice((Long) document.get("item_price"));
			searchItem.setSell_point((String) document.get("item_sell_point"));
			//取高亮结果
			String item_title = null;
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(document.get("id")).get("item_title");
			if(list!=null && list.size()>0){
				item_title = list.get(0);
			}else{
				item_title=(String) document.get("item_title");
			}
			searchItem.setTitle(item_title);
			searchItemList.add(searchItem);
		}
		//设置返回结果
		SearchResult result = new SearchResult();
		result.setRecourdCount(recourdCount);
		result.setItemList(searchItemList);
		
		return result;
	}

}
