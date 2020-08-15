package com.cache.cahcing.connector;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RedissionConnector {

  RedissonClient redisson;

  @PostConstruct
  public void init() {
    try {
      Config config = new Config();

      config.setCodec(new org.redisson.client.codec.StringCodec());

      //指定使用单节点部署方式
      config.useSingleServer().setAddress("127.0.0.1:7000");
      config.useSingleServer().setConnectionPoolSize(500);
      config.useSingleServer().setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
      config.useSingleServer().setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
      config.useSingleServer().setTimeout(10000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
      config.useSingleServer().setPingTimeout(30000);
      config.useSingleServer().setReconnectionTimeout(3000);//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。

      config.useSingleServer().setConnectionMinimumIdleSize(50);
      redisson = Redisson.create(config);
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public RedissonClient getClient() {
    return redisson;
  }

}
