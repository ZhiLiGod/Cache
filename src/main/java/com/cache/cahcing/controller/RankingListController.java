package com.cache.cahcing.controller;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/ranking")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankingListController {

  private static final String SCORE_RANK = "score_rank";

  private final StringRedisTemplate template;

  @GetMapping("/show")
  public ResponseEntity<Void> show() {

    batchAdd();
    top10();
    count();

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void batchAdd() {

    Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();

    for (int i = 0; i < 100000; i++) {

      DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>("Tom" + i, 1D + i);
      tuples.add(tuple);
    }

    template.opsForZSet().add(SCORE_RANK, tuples);
  }

  private void top10() {

    Set<String> range = template.opsForZSet().range(SCORE_RANK, 0, 10);
    log.info("top 10 is: {}", JSON.toJSONString(range));
  }

  private void count() {

    Long count = template.opsForZSet().count(SCORE_RANK, 8001, 9000);
    log.info("8001 to 9000: {}", count);
  }

}
