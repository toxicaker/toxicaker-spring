package com.toxicaker.aop;

import com.toxicaker.core.Component;

@Component
public class AopInterfaceServiceImpl implements AopInterfaceService {

  @Override
  public void testInterfaceMethod() {
    System.out.println("testInterfaceMethod call");
  }
}
