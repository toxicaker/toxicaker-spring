package com.toxicaker.example.di.negative;

import com.toxicaker.core.Component;
import com.toxicaker.core.Inject;

@Component
public class E {

  @Inject
  private C c;

  @Inject
  private D d;

  public C getC() {
    return c;
  }

  public void setC(C c) {
    this.c = c;
  }

  public D getD() {
    return d;
  }

  public void setD(D d) {
    this.d = d;
  }
}
