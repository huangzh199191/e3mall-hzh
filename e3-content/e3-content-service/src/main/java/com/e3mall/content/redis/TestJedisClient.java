package com.e3mall.content.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisClient {
	
	@Test
	public void testJedisClient(){
		//初始化Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class );
		jedisClient.set("first", "100");
		String result = jedisClient.get("first");
		System.out.println(result);

	}
}
