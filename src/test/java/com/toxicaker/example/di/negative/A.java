package com.toxicaker.example.di.negative;

import com.toxicaker.core.Component;
import com.toxicaker.core.Inject;

@Component
public class A {

  @Inject
  private B b;

  @Inject
  private C c;

  public B getB() {
    return b;
  }

  public void setB(B b) {
    this.b = b;
  }

  public C getC() {
    return c;
  }

  public void setC(C c) {
    this.c = c;
  }
}
