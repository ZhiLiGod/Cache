package com.cache.cahcing.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisLock {

  private String lockKey = "redisLock";
  protected long lockExpireTime = 30000;
  private long timeout = 99999;

  private final JedisPool jedisPool;

  private SetParams params = SetParams.setParams().nx().px(lockExpireTime);

  public boolean lock(String id) {

    Jedis jedis = jedisPool.getResource();
    long startTime = System.currentTimeMillis();

    try {
      for (;;) {

        String lock = jedis.set(lockKey, id, params);

        if ("OK".equals(lock)) {
          return true;
        }

        long time = System.currentTimeMillis() - startTime;

        // cant get lock in timeout
        if (time >= timeout) {
          return false;
        }

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          log.error("Error when acquire lock", e);
        }
      }
    } finally {
      jedis.close();
    }
  }

  public boolean unlock(String id) {
    Jedis jedis = jedisPool.getResource();
    String script =
      "if redis.call('get',KEYS[1]) == ARGV[1] then" +
        "   return redis.call('del',KEYS[1]) " +
        "else" +
        "   return 0 " +
        "end";
    try {
      Object result = jedis.eval(script, Collections.singletonList(lockKey),
        Collections.singletonList(id));
      if ("1".equals(result.toString())) {
        return true;
      }
      return false;
    } finally {
      jedis.close();
    }
  }

}
