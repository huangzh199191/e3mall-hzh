package com.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
	//添加文档
	@Test
	public void testAddDocument() throws Exception{
		//1.创建一个SolrServer，使用HttpSolrServer创建对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		//2.创建一个文档对象SolrInputDocument对象
		SolrInputDocument document = new SolrInputDocument();
		//3.向文档中添加域。必须有id域，域的名称必须在schema.xml中定义
		document.addField("id", "test001");
		document.addField("item_title", "测试商品");
		document.addField("item_price", "100");
		//4.把文档添加到索引库中
		solrServer.add(document);
		//5.提交
		solrServer.commit();
	}
	//删除文档
	@Test
	public void deleteDocument() throws Exception{
//		创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
//		调用SolrServer对象的根据id删除的方法。
		solrServer.deleteById("test001");
//		提交
		solrServer.commit();
	}
	//根据查询删除
	@Test
	public void deleteDocumentByQuery() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		solrServer.deleteByQuery("item_title:测试");
		solrServer.commit();
	}
	//查询索引库
	@Test
	public void queryDocument() throws Exception{
//		第一步：创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
//		第二步：创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
//		第三步：向SolrQuery中添加查询条件、过滤条件。。。
		query.setQuery("*:*");
//		第四步：执行查询。得到一个Response对象。
		QueryResponse queryResponse = solrServer.query(query);
//		第五步：取查询结果。
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询到的总记录数："+solrDocumentList.getNumFound());
//		第六步：遍历结果并打印。
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
	//查询返回高亮结果
	@Test
	public void QueryDocumenWithtHighlighting() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("测试");
		//设置默认搜索域
		query.set("df","item_keywords");
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList documentList = queryResponse.getResults();
		System.out.println("查询到的总记录数"+documentList.getNumFound());
		//遍历查询结果
		for (SolrDocument document : documentList) {
			System.out.println(document.get("id"));
			//取高亮结果
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(document.get("id")).get("item_title");
			String item_title = null;
			if(list!=null && list.size()>0){
				item_title=list.get(0);
			}else{
				item_title=(String) document.get("item_title");
			}
			System.out.println(item_title);
			System.out.println(document.get("item_price"));
		}
	}
}
