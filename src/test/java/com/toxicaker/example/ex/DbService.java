package com.toxicaker.example.ex;

import com.toxicaker.core.BeanNameAware;
import com.toxicaker.core.Component;
import com.toxicaker.core.Component.Type;

@Component(type = Type.PROTOTYPE)
public class DbService  implements BeanNameAware {

  public String name;

  @Override
  public void setBeanName(String name) {
    this.name = name;
    System.out.println("name: " + name);
  }
}
