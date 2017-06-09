package com.e3mall.item.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemChangeMessageListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	
	
	@Override
	public void onMessage(Message message) {
		try {
			//1.从消息中获得商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = Long.valueOf(text);
			//2.根据id查询商品对象，查询商品描述对象
			//等待事务提交
			TbItem tbItem = null;
			for(int i=0;i<3;i++){
				Thread.sleep(1000);
				tbItem = itemService.getItemById(itemId);
				if(tbItem!=null){
					break;
				}
			}
			Item item = new Item(tbItem);
			E3Result result = itemService.getItemDescById(itemId);
			TbItemDesc itemDesc =  (TbItemDesc) result.getData();
			//3.根据freemarker生成静态页面
			//获得configuration对象
			Configuration configuration = freeMarkerConfig.getConfiguration();
			//根据configuration获得模板
			Template template = configuration.getTemplate("item.ftl");
			//模板需要的数据
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//创建输出流对象
			Writer out = new OutputStreamWriter(new FileOutputStream(new File(HTML_OUT_PATH+text+".html")),"UTF-8");
			//生成静态页面
			template.process(data, out);
			//关流
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
