package com.cache.cahcing.controller;

import com.cache.cahcing.lock.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
@RequestMapping("/lock")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LockController {

  private final RedisLock redisLock;

  int count = 0;

  @GetMapping
  public String index() throws InterruptedException {

    CountDownLatch countDownLatch = new CountDownLatch(10);
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 10; i++) {

      executorService.execute(() -> {

        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        try {
          redisLock.lock(id);
          count++;
        } finally {
          redisLock.unlock(id);
        }

        countDownLatch.countDown();
      });
    }

    countDownLatch.await();

    return "SUCCESS";
  }

}
