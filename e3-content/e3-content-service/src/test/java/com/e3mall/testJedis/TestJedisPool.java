package com.e3mall.testJedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedisPool {
	
	@Test
	public void testJedisPool(){
		//创建jedis连接池对象，指定服务ip和端口
		JedisPool jedisPool = new JedisPool("192.168.25.129", 6379);
		//从连接池中获取jedis对象
		Jedis jedis = jedisPool.getResource();
		//操作数据库
		jedis.set("name", "jack");
		String name = jedis.get("name");
		System.out.println(name);
		//关闭jedis
		jedis.close();
		//系统关闭是关闭连接池
		jedisPool.close();
	}
}
