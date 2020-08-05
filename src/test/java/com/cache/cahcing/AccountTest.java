package com.cache.cahcing;

import com.cache.cahcing.bean.Account;
import com.cache.cahcing.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CahcingApplication.class })
public class AccountTest {

  @Autowired
  private AccountService accountService;

  @Test
  public void test() throws Exception {

    final String value = RandomStringUtils.randomAlphabetic(3);

    Account account = accountService.getByName(value);
    Account account1 = accountService.getByName(value);

    assertEquals(account.getName(), value);
    assertEquals(account1.getName(), value);
  }

}
