package com.cache.cahcing.memcache;

import com.cache.cahcing.CahcingApplication;
import com.whalin.MemCached.MemCachedClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CahcingApplication.class })
public class MemcacheCommandTest {

  @Autowired
  private MemCachedClient memCachedClient;

  @Test
  public void test() {

    memCachedClient.set("flagKey", 1);
    Object flagValue = memCachedClient.get("flagKey");
    log.info("flag value: {}", flagValue);
    assertThat(flagValue.toString()).isEqualTo("1");

    memCachedClient.set("testKey", "1", new Date(10000)); // wait 1 sec
    log.info("testValue: {}", memCachedClient.get("testKey"));

    memCachedClient.set("flagKey", 10);
    log.info("flagValue: {}", memCachedClient.get("flagKey")); // 10

    memCachedClient.delete("flagKey");
    log.info("flagKey: {}", memCachedClient.get("flagKey"));
  }

}
