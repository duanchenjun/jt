package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
@TestComponent
@RunWith(SpringRunner.class)
public class TestRedis {
	
	private Jedis jedis;
	@Before
	public void init() {
		jedis=new Jedis("192.168.153.129",6379);
	}
	
	/**
	 * 1.测试String类型
	 * 参数说明:
	 * host: redis的ip地址
	 * port: redis端口号
	 * @throws InterruptedException 
	 * 分布式锁:
	 * 	redis创建一个锁
	 * 	jedis.setnx
	 * 	jedis.setex
	 */
	@Test
	public void testString() throws InterruptedException {
		jedis.set("1906", "redis入门案例");
		String value = jedis.get("1906");
		System.out.println(value);
		
		//2.测试key相同时value是否覆盖 	值被覆盖
		jedis.set("1906", "redis测试");
		System.out.println(jedis.get("1906"));
		
		//3.如果值已经存在 则不允许覆盖
		jedis.setnx("1906", "NBA不转播了!!!");
		System.out.println(jedis.get("1906"));
		
		//4.为数据添加超时时间
		jedis.set("time", "超时测试");
		jedis.expire("1906", 60);
		
		//保证数据操作有效性(原子性)
		jedis.setex("time", 100, "超时测试");
		Thread.sleep(3000);
		Long time = jedis.ttl("time");
		System.out.println("当前数据还能存活:"+time+"秒");
		
		//5.要求key存在是不允许操作,并且设定超时时间
		//nx:不允许覆盖	xx:可以覆盖
		//ex:单位秒 		px:单位毫秒
		jedis.set("时间", "测试是否有效","NX", "EX", 100);
		System.out.println(jedis.get("时间"));
	}
	
	@Test
	public void testHash() {
		Map<String, String> hgetAll=jedis.hgetAll("user");
		System.out.println(hgetAll);
		jedis.hset("person", "id", "100");
		jedis.hset("person", "name", "超人");
		System.out.println(jedis.hgetAll("person"));
	}
	@Test
	public void testList() {
		jedis.rpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
		jedis.rpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
		
	}
	@Test
	public void testTx() {
		//1.开启事务
		Transaction transaction=jedis.multi();
		try {
			transaction.set("a", "aaa");
			transaction.set("b", "bbb");
			transaction.set("c", "ccc");
			//int a=1/0;
			//2.事务提交
			List<Object> exec = transaction.exec();
			System.out.println(exec);
		} catch (Exception e) {
			e.printStackTrace();
			String discard = transaction.discard();
			System.out.println(discard);
		}
	}
	
	/**
	 * 2.实现redis分片操作
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> list=new ArrayList<JedisShardInfo>();
		list.add(new JedisShardInfo("192.168.153.129", 6379));
		list.add(new JedisShardInfo("192.168.153.129", 6380));
		list.add(new JedisShardInfo("192.168.153.129", 6381));
		ShardedJedis jedis=new ShardedJedis(list);
		jedis.set("1906", "redis分片测试");
		System.out.println(jedis.get("1906"));
	}
	
	/**
	 * 测试哨兵
	 * 通用原理:
	 * 	用户通过哨兵,连接redis的主机,进行操作
	 * 	masterName:主机的变量名称
	 * 	sentinels:	redis节点信息
	 * 				Set<String>
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels=new HashSet<>();
		sentinels.add("192.168.153.129:26379");
		JedisSentinelPool sentinelPool=new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis=sentinelPool.getResource();
		jedis.set("1906", "哨兵测试成功");
		System.out.println(jedis.get("1906"));
	}
	@Test
	public void testCluster() {
		Set<HostAndPort> node=new HashSet<>();
		node.add(new HostAndPort("192.168.153.129", 7000));
		node.add(new HostAndPort("192.168.153.129", 7001));
		node.add(new HostAndPort("192.168.153.129", 7002));
		node.add(new HostAndPort("192.168.153.129", 7003));
		node.add(new HostAndPort("192.168.153.129", 7004));
		node.add(new HostAndPort("192.168.153.129", 7005));
		JedisCluster cluster=new JedisCluster(node);
		cluster.set("1906", "redis集群测试成功");
		System.out.println(cluster.get("1906"));
	}
	
	
}
