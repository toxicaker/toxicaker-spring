package com.toxicaker.core;

public interface BeanPostProcessor {

  Object beanPostProcessorBeforeInit(Object bean, String beanName);

  Object beanPostProcessorAfterInit(Object bean, String beanName);

  int order();
}
