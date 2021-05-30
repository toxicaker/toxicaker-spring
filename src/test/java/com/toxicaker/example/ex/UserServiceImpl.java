package com.toxicaker.example.ex;

import com.toxicaker.core.Component;
import com.toxicaker.core.InitializingBean;

@Component
public class UserServiceImpl implements UserService, InitializingBean {

  public String aaa;

  @Override
  public void afterPropertiesSet() {
    this.aaa = "eq";
  }
}
