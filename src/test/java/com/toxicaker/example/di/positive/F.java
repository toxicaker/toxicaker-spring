package com.toxicaker.example.di.positive;

import com.toxicaker.core.Component;
import com.toxicaker.core.Inject;

@Component
public class F {

  @Inject
  private G g;

  public G getG() {
    return g;
  }

  public void setG(G g) {
    this.g = g;
  }
}
