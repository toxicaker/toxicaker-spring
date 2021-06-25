package com.toxicaker.aop;

import com.toxicaker.core.Component;

@Aspect
@Component
public class UserServiceAspect {

  @Before("testMethod")
  void beforeTestMethod() {
    System.out.println("Before method call testMethod");
  }

  @After("testMethod")
  void afterTestMethod() {
    System.out.println("After method call testMethod");
  }

  @Before("testInterfaceMethod")
  void beforeFunc() {
    System.out.println("Before method call testInterfaceMethod");
  }

  @After("testInterfaceMethod")
  void afterFunc() {
    System.out.println("After method call testInterfaceMethod");
  }
}
