package com.e3mall.testActivemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestSpringActiveMQ {
	
	@Test
	public void testQueueProducer() throws Exception{
		//获得spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//获得jmsTemplate
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//获得队列目的地
		Queue queue = (Queue) applicationContext.getBean("queueDestination");
		//使用jmsTemplate发送消息
		jmsTemplate.send(queue, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("hello spring activemq");
				return textMessage;
			}
		});
	}
}
