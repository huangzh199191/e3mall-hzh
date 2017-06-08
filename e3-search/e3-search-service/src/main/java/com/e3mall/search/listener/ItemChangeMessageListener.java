package com.e3mall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.SearchItemMapper;
import com.e3mall.search.service.SearchItemService;

/*
 * 添加商品后的消息监听器
 */
public class ItemChangeMessageListener implements MessageListener {
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SearchItemService searchItemService;
	
	@Override
	public void onMessage(Message message) {
		try {
			//1.从消息中取出商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			long itemId = Long.valueOf(text);
			//2.根据商品id查询商品详情，这里需要注意的是消息发送方法  
			//有可能还没有提交事务，因此这里是有可能取不到商品信息  
			//的，为了避免这种情况出现，我们最好等待事务提交，这里  
			//采用3次尝试的方法，每尝试一次休眠一秒 
			SearchItem searchItem=null;
			for(int i=0;i<3;i++){
				try {
					Thread.sleep(1000);
					searchItem = searchItemMapper.getSearchItemById(itemId);
					//获得商品信息，跳出循环
					if(searchItem!=null){
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//3.将查询到的商品添加到索引库
			searchItemService.addSearchItemToIndex(searchItem);
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
