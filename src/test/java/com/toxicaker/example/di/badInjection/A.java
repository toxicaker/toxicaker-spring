package com.toxicaker.example.di.badInjection;

import com.toxicaker.core.Component;
import com.toxicaker.core.Inject;

@Component
public class A {

  @Inject
  private B b;


  public B getB() {
    return b;
  }

  public void setB(B b) {
    this.b = b;
  }


}
