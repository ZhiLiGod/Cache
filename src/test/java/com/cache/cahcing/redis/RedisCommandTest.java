package com.cache.cahcing.redis;

import com.cache.cahcing.CahcingApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CahcingApplication.class })
public class RedisCommandTest {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Test
  public void test() {

    // String
    redisTemplate.opsForValue().set("abc", "bbb");
    log.info("set: {}", redisTemplate.opsForValue().get("abc"));
    assertThat(redisTemplate.opsForValue().get("abc")).isEqualTo("bbb");

    // number
    redisTemplate.opsForValue().set("initValue", "1");
    redisTemplate.opsForValue().increment("initValue");
    log.info("init value: {}", redisTemplate.opsForValue().get("initValue"));
    assertThat(redisTemplate.opsForValue().get("initValue")).isEqualTo("2");

    // list
    redisTemplate.opsForList().rightPush("list", "aaa");
    redisTemplate.opsForList().rightPush("list", "bbb");
    redisTemplate.opsForList().rightPush("list", "ccc");
    redisTemplate.opsForList().rightPush("list", "ddd");
    log.info("list rpop: {}", redisTemplate.opsForList().rightPop("list")); // delete the last pushed element: ddd
    log.info("list + index: {}", redisTemplate.opsForList().index("list", 1)); // bbb
    log.info("list + range: {}", redisTemplate.opsForList().range("list", 0, 1)); // aaa bbb

    // hash
    redisTemplate.opsForHash().put("map", "aaa", "value-a");
    redisTemplate.opsForHash().put("map", "bbb", "value-b");
    redisTemplate.opsForHash().put("map", "ccc", "value-c");
    log.info("hash key: {}", redisTemplate.opsForHash().keys("map")); // bbb aaa ccc
    log.info("hash value: {}", redisTemplate.opsForHash().get("map", "aaa")); // value-a
    log.info("if key exist: {}", redisTemplate.opsForHash().hasKey("map", "bbb"));
    log.info("hash values: {}", redisTemplate.opsForHash().values("map"));

    // set
    redisTemplate.opsForSet().add("set", "aaa");
    redisTemplate.opsForSet().add("set", "bbb");
    redisTemplate.opsForSet().add("set", "ccc");
    log.info("set pop: {}", redisTemplate.opsForSet().pop("set")); // last element: ccc

    // zset
    redisTemplate.opsForZSet().add("zset", "aaa", 2);
    redisTemplate.opsForZSet().add("zset", "bbb", 1);
    redisTemplate.opsForZSet().add("zset", "ccc", 3);
    log.info("zset rank: {}", redisTemplate.opsForZSet().rank("zset", "ccc")); // 2 (start from 0)
    log.info("zset zcard: {}", redisTemplate.opsForZSet().zCard("zset")); // total elements

  }

}
