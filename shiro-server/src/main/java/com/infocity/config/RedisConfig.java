package com.infocity.config;

import com.infocity.utils.JedisUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author liaoqiangang
 * @date 2019/12/11 2:45 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String host;

    private Integer port;

    private Integer timeout;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout * 1000);
        return jedisPool;
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    }

}
