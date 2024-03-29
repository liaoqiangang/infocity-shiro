package com.infocity.cache;

import com.infocity.utils.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @author liaoqiangang
 * @date 2019/12/11 5:48 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Component
public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final String CACHE_PREFIX = "shiro-cache";
    private final int EXPIRE_TIME = 600;

    @Resource
    private JedisUtil jedisUtil;

    private byte[] getKey(K k) {
        if (k instanceof String) {
            return (CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        logger.info("从redis缓存中获取数据");
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, EXPIRE_TIME);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        if(value!=null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    /**
     * 不要重新它，调用会清空整个数据库
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
