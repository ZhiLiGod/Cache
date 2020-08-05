package com.cache.cahcing.service;

import com.cache.cahcing.bean.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AccountService {

  @Cacheable(value = "accountCache")
  public Account getByName(String name) throws Exception {

    log.info("start");
    Account account = getFromDB(name);

    if (Objects.isNull(account)) {
      throw new Exception("no account was found");
    } else {
      return account;
    }
  }

  public Account getFromDB(String accountName) {

    log.info("getting data from db");
    return Account.builder()
      .id(1)
      .name(accountName)
      .build();
  }

  @CachePut(value = "accountCache", key = "#accountName")
  public void saveAccountByName(String accountName) {

    log.info("save account: {}", accountName);
  }

}
