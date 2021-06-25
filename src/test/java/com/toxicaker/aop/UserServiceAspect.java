package com.toxicaker.aop;

import com.toxicaker.core.Component;

@Aspect
@Component
public class UserServiceAspect {

  @Before("com.toxicaker.aop.AopService.testMethod")
  void beforeTestMethod() {
    System.out.println("Before method call testMethod");
  }

  @After("com.toxicaker.aop.AopService.testMethod")
  void afterTestMethod() {
    System.out.println("After method call testMethod");
  }

  @Before("com.toxicaker.aop.AopInterfaceServiceImpl.testInterfaceMethod")
  void beforeFunc() {
    System.out.println("Before method call testInterfaceMethod");
  }

  @After("com.toxicaker.aop.AopInterfaceServiceImpl.testInterfaceMethod")
  void afterFunc() {
    System.out.println("After method call testInterfaceMethod");
  }
}
