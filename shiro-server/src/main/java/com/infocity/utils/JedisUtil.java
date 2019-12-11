package com.infocity.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author liaoqiangang
 * @date 2019/12/11 2:44 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;


    private Jedis getResource() {
        return jedisPool.getResource();
    }

    /**
     * 存值
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }

    /**
     * @param key
     * @param expire
     */
    public void expire(byte[] key, int expire) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, expire);
        } finally {
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(String perfix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((perfix + "*").getBytes());
        } finally {
            jedis.close();
        }
    }
}
