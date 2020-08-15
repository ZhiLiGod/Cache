package com.cache.cahcing.service;

public interface DistributedLocker {

  <T> T lock(String resourceName, AcquiredLockWorker<T> worker) throws Exception;

  <T> T lock(String resourceName, AcquiredLockWorker<T> worker, int lockTime) throws Exception;

}
