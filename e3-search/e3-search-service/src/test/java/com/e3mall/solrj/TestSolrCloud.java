package com.e3mall.solrj;


import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	@Test
	public void testSolrCloudAddDocment() throws Exception{
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.129:2182,192.168.25.129:2183,192.168.25.129:2184");
		solrServer.setDefaultCollection("collection2");
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "test001");
		document.addField("item_title", "测试数据");
		solrServer.add(document);
		solrServer.commit();
	}
	@Test
	public void testSolrCloudDeleteDocment() throws Exception{
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.129:2182,192.168.25.129:2183,192.168.25.129:2184");
		solrServer.setDefaultCollection("collection2");
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
	}
}
