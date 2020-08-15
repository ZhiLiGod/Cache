package com.cache.cahcing.service;

public interface AcquiredLockWorker<T> {

  T invokeAfterLockAcquire() throws Exception;

}
