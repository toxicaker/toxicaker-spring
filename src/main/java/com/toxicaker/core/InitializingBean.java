package com.toxicaker.core;

/**
 * Callback interface. Once bean is initialized, ApplicationContext will try to call
 * afterPropertiesSet(). Usage:
 * <pre>
 *   class UserService implement InitializingBean {
 *     public void afterPropertiesSet() {
 *       System.out.println("hello world");
 *     }
 *   }
 * </pre>
 */
public interface InitializingBean {

  void afterPropertiesSet();
}
