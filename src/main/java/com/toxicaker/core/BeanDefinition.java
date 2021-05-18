package com.toxicaker.core;

public class BeanDefinition {

  private final Class<?> clazz;

  private final String name;

  private final Component.Type type;

  public BeanDefinition(Class<?> clazz, String name, Component.Type type) {
    this.clazz = clazz;
    this.name = name;
    this.type = type;
  }

  public BeanDefinition(Class<?> clazz, Component.Type type) {
    this.clazz = clazz;
    this.type = type;
    this.name = getBeanName(clazz);
  }

  private String getBeanName(Class<?> clazz) {
    return clazz.getName();
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public String getName() {
    return name;
  }

  public Component.Type getType() {
    return type;
  }

}
