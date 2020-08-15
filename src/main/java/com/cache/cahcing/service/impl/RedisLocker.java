package com.cache.cahcing.service.impl;

import com.cache.cahcing.connector.RedissionConnector;
import com.cache.cahcing.service.AcquiredLockWorker;
import com.cache.cahcing.service.DistributedLocker;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisLocker implements DistributedLocker {

  private final RedissionConnector redissionConnector;

  @Override
  public <T> T lock(String resourceName, AcquiredLockWorker<T> worker) throws Exception {
    return lock(resourceName, worker, 100);
  }

  @Override
  public <T> T lock(String resourceName, AcquiredLockWorker<T> worker, int lockTime) throws Exception {

    RedissonClient redissonClient = redissionConnector.getClient();
    RLock lock = redissonClient.getLock("lock" + resourceName);

    boolean success = lock.tryLock(100, lockTime, TimeUnit.SECONDS);

    if (success) {

      try {

        return worker.invokeAfterLockAcquire();
      } finally {

        lock.unlock();
      }
    }

    throw new Exception();
  }

}
