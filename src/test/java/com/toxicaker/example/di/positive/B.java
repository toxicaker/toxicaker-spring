package com.toxicaker.example.di.positive;

import com.toxicaker.core.Component;
import com.toxicaker.core.Component.Type;
import com.toxicaker.core.Inject;

@Component(type = Type.PROTOTYPE)
public class B {

  @Inject
  private D d;

  @Inject
  private E e;

  public D getD() {
    return d;
  }

  public void setD(D d) {
    this.d = d;
  }

  public E getE() {
    return e;
  }

  public void setE(E e) {
    this.e = e;
  }
}
