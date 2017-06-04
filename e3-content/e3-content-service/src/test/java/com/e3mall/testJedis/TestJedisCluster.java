package com.e3mall.testJedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;


public class TestJedisCluster {
	@Test
	public void testJedisCluster(){
		//1.使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.129", 7001));
		nodes.add(new HostAndPort("192.168.25.129", 7002));
		nodes.add(new HostAndPort("192.168.25.129", 7003));
		nodes.add(new HostAndPort("192.168.25.129", 7004));
		nodes.add(new HostAndPort("192.168.25.129", 7005));
		nodes.add(new HostAndPort("192.168.25.129", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//2.直接使用JedisCluster对象操作redis。在系统中单例存在
		jedisCluster.set("name", "jack");
		String name = jedisCluster.get("name");
		//3.打印结果
		System.out.println(name);
		//4.系统关闭前，关闭JedisCluster对象。
		jedisCluster.close();
	}
}
