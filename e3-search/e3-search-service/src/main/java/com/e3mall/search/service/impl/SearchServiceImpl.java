package com.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;
import com.e3mall.search.service.SearchService;

/*
 * 搜索的service
 */
@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//根据参数创建查询条件,分页,默认查询域 ，高亮显示
		SolrQuery query = new SolrQuery();
		query.setQuery(keyword);
		query.setStart((page-1)*rows);
		query.setRows(rows);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style='color:red'>");
		query.setHighlightSimplePost("</em>");
		//调用dao查询
		SearchResult result = searchDao.search(query);
		//设置返回结果
		//计算总页数
		long recourdCount = result.getRecourdCount();
		Long totalPages = recourdCount/rows;
		if(recourdCount % rows >0) totalPages++;
		result.setTotalPages(totalPages);
		return result;
	}

}
