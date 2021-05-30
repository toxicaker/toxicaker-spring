package com.toxicaker.example.di.negative;

import com.toxicaker.core.Component;
import com.toxicaker.core.Inject;

@Component
public class D {

  @Inject
  private A a;

  public A getA() {
    return a;
  }

  public void setA(A a) {
    this.a = a;
  }
}
