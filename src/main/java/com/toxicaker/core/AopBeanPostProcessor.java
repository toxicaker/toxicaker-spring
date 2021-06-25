package com.toxicaker.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class AopBeanPostProcessor implements BeanPostProcessor {

  // function name -> proxy functions
  private final Map<String, Set<Method>> beforeFuncMap = new HashMap<>();
  private final Map<String, Set<Method>> afterFuncMap = new HashMap<>();
  private final Map<Method, Object> beanMap = new HashMap<>();


  @Override
  public Object beanPostProcessorBeforeInit(Object bean, String beanName) {
    var clazz = bean.getClass();
    if (clazz.getAnnotation(Aspect.class) != null) {
      var methods = clazz.getMethods();
      for (var m : methods) {
        var before = m.getAnnotation(Before.class);
        if (before != null) {
          Set<Method> beforeMethods = beforeFuncMap.getOrDefault(before.methodName(), new HashSet<>());
          beforeMethods.add(m);
          beforeFuncMap.put(before.methodName(), beforeMethods);
        }
        var after = m.getAnnotation(After.class);
        if (after != null) {
          Set<Method> afterMethods = afterFuncMap.getOrDefault(after.methodName(), new HashSet<>());
          afterMethods.add(m);
          afterFuncMap.put(after.methodName(), afterMethods);
        }
      }
    }
    return bean;
  }

  @Override
  public Object beanPostProcessorAfterInit(Object bean, String beanName) {
    var factory = new ProxyFactory(bean);
    return factory.getProxyInstance();
  }

  @Override
  public int order() {
    return 0;
  }

  private class ProxyFactory implements MethodInterceptor {

    private final Object target;

    public ProxyFactory(Object target) {
      this.target = target;
    }

    public Object getProxyInstance() {
      Enhancer en = new Enhancer();
      en.setSuperclass(target.getClass());
      en.setCallback(this);
      return en.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable {
      // before
      var beforeMethods = beforeFuncMap.getOrDefault(method.getName(), new HashSet<>());
      for (var m : beforeMethods) {
        System.out.println("Invoked before method: " + m.getName());
        m.invoke(beanMap.get(m));
      }
      System.out.println("Invoked method: " + method.getName());
      Object result = method.invoke(target, args);
      // after
      var afterMethods = afterFuncMap.getOrDefault(method.getName(), new HashSet<>());
      for (var m : afterMethods) {
        System.out.println("Invoked after method: " + m.getName());
        m.invoke(beanMap.get(m));
      }
      return result;
    }
  }
}
