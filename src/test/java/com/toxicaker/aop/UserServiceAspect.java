package com.toxicaker.aop;

import com.toxicaker.core.Component;

@Aspect
@Component
public class UserServiceAspect {

  @Before("testMethod")
  void beforeFunc() {
    System.out.println("Before method call");
  }

  @After("testMethod")
  void afterFunc() {
    System.out.println("After method call");
  }
}
