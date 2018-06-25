package ru.airiva.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.airiva.entity.SessionData;

@Component
public class AreaRedisRepo {

    private RedisTemplate<String, SessionData> redisTemplate;

    private static String AREA_KEY = "Area";

    public RedisTemplate<String, SessionData> getRedisTemplate() {
        return redisTemplate;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, SessionData> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void put(String tlgId, SessionData sessionData) {
        this.redisTemplate.opsForHash().put(tlgId, 1, sessionData);
    }


    public SessionData get(String tlgId) {
        return (SessionData) this.redisTemplate.opsForHash().get(tlgId, 1);
    }

    public Boolean remove(String tlgId) {
        return this.redisTemplate.delete(tlgId);
    }
}
