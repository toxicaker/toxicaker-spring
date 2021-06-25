package com.toxicaker.core;

/**
 * BeanPostProcessor is used to add extra steps before beans created and after beans created.
 * Usage:
 * <pre>
 *   @ Component
 *   public class TestPostProcessor implements BeanPostProcessor{
 *
 *      public Object beanPostProcessorBeforeInit(Object bean, String beanName);
 *
 *      public Object beanPostProcessorAfterInit(Object bean, String beanName);
 *
 *      public int order();
 *   }
 * </pre>
 * Multiple BeanPostProcessors are allowed. The order matters which can be specified in "int
 * order()" method. 0 means top priority. Processors will be invoked based on the order:
 * <pre>
 *   BeanPostProcessorA -> BeanPostProcessorB -> BeanPostProcessorC
 * </pre>
 */
public interface BeanPostProcessor {

  Object beanPostProcessorBeforeInit(Object bean, String beanName);

  Object beanPostProcessorAfterInit(Object bean, String beanName);

  int order();
}
