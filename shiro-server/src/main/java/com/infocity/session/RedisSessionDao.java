package com.infocity.session;

import com.infocity.utils.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liaoqiangang
 * @date 2019/12/11 2:42 PM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
public class RedisSessionDao extends AbstractSessionDAO {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String SHIRO_SESSION_PERFIX = "infocity-session:";

    @Autowired
    private JedisUtil jedisUtil;

    public byte[] getKey(String key) {
        return (SHIRO_SESSION_PERFIX + key).getBytes();
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
//        logger.info("create session");
        Serializable sesssionId = generateSessionId(session);
        assignSessionId(session, sesssionId);
        saveSession(session);
        return sesssionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.info("read session");
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
//        logger.info("update session");
        saveSession(session);
    }


    @Override
    public void delete(Session session) {
//        logger.info("delete session");
        if (session == null && session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PERFIX);
        Set<Session> sessions = new HashSet<>(16);
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (byte[] key : keys) {
            byte[] value = jedisUtil.get(key);
            Session session = (Session) SerializationUtils.deserialize(value);
            sessions.add(session);
        }
        return sessions;
    }
}
