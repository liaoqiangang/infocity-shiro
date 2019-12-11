package com.infocity.redis;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @author liaoqiangang
 * @date 2019/12/11 4:27 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJedis {

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void testJedis(){
        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
        jedis.set("key","value");
        System.out.println("redis存储");
    }

    @Test
    public void testBean(){
    }
}
