package com.toxicaker.core;

/**
 * Callback interface. Once bean is initialized, ApplicationContext will try to call
 * setBeanName(String name) method and put the bean name as parameter. Usage:
 * <pre>
 *   class UserService implement BeanNameAware {
 *     public void setBeanName(String name) {
 *       System.out.println("The bean name is: " + name);
 *     }
 *   }
 * </pre>
 */
public interface BeanNameAware {

  void setBeanName(String name);
}
