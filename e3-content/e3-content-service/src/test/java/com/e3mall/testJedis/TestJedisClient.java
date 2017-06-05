package com.e3mall.testJedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e3mall.common.jedis.JedisClient;

public class TestJedisClient {
	
	@Test
	public void testJedisClient(){
		//读取配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//获得jedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//操作数据库
		jedisClient.set("name", "hello");
		String string = jedisClient.get("name");
		System.out.println(string);
	}
}
