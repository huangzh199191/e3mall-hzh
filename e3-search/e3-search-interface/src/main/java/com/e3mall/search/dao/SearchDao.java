package com.e3mall.search.dao;


import org.apache.solr.client.solrj.SolrQuery;

import com.e3mall.common.pojo.SearchResult;

public interface SearchDao {
	SearchResult search(SolrQuery query) throws Exception;
}
