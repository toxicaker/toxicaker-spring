package com.toxicaker.aop;

import com.toxicaker.core.BeanPostProcessor;
import com.toxicaker.core.Component;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * AOP processor.
 * AopBeanPostProcessor implements BeanPostProcessor which will be called when beans are creating. I
 * will firstly scan all the beans that have @Aspect annotation. Then pack all the "before methods"
 * and "after methods" into function maps. ProxyFactory utilized CGLIB dynamic proxy.
 */
@Component
public class AopBeanPostProcessor implements BeanPostProcessor {

  // function name -> proxy functions
  private final Map<String, Set<Method>> beforeFuncMap = new HashMap<>();
  private final Map<String, Set<Method>> afterFuncMap = new HashMap<>();
  private final Map<Method, Object> beanMap = new HashMap<>();

  @Override
  public Object beanPostProcessorBeforeInit(Object bean, String beanName) {
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

  public void scan(Class<?> clazz, Object bean) {
    if (clazz.getDeclaredAnnotation(Aspect.class) != null) {
      var methods = clazz.getDeclaredMethods();
      for (var m : methods) {
        var before = m.getDeclaredAnnotation(Before.class);
        if (before != null) {
          Set<Method> beforeMethods = beforeFuncMap
              .getOrDefault(before.value(), new HashSet<>());
          beforeMethods.add(m);
          beforeFuncMap.put(before.value(), beforeMethods);
        }
        var after = m.getDeclaredAnnotation(After.class);
        if (after != null) {
          Set<Method> afterMethods = afterFuncMap.getOrDefault(after.value(), new HashSet<>());
          afterMethods.add(m);
          afterFuncMap.put(after.value(), afterMethods);
        }
        beanMap.put(m, bean);
      }
    }
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
      int idx = obj.getClass().getName().indexOf("$");
      String funcName = obj.getClass().getName().substring(0, idx) + "." + method.getName();
      // before
      var beforeMethods = beforeFuncMap.getOrDefault(funcName, new HashSet<>());
      for (var m : beforeMethods) {
        m.invoke(beanMap.get(m));
      }
      Object result = method.invoke(target, args);
      // after
      var afterMethods = afterFuncMap.getOrDefault(funcName, new HashSet<>());
      for (var m : afterMethods) {
        m.invoke(beanMap.get(m));
      }
      return result;
    }
  }
}
