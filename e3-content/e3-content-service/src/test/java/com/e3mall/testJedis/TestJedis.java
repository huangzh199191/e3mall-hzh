package com.e3mall.testJedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestJedis {
	@Test
	public void testJedis(){
		//创建jedis对象，指定服务ip和端口
		Jedis jedis = new Jedis("192.168.25.129", 6379);
		//使用jedis对象操作数据库
		jedis.set("name", "rose");
		String name = jedis.get("name");
		System.out.println(name);
		//关闭jedis
		jedis.close();
	}
}
