package com.toxicaker.core;

import com.google.common.annotations.VisibleForTesting;
import com.toxicaker.core.Component.Type;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entrance of Spring framework. When ApplicationContext's constructor is called, it will scan
 * all the java files with @Component annotation, create either "singleton" or "prototype" objects
 * and save them into object pools
 * <p>
 * Dependency Injection replies on the object pool. If a class field has @Inject annotation,
 * getBean(String) method will be called. It will try to find the object that already saved in the
 * object pool, then assign the object to the variable.
 */
public class ApplicationContext {

  private final Map<String, Object> objectPool = new HashMap<>();
  private final Map<String, BeanDefinition> beanDefinitionPool = new HashMap<>();

  private static final String ROOT_PACKAGE = "com.toxicaker";

  private final static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

  public ApplicationContext() throws Exception {
    scan(null);
  }

  public ApplicationContext(Class<?> appConfig) throws Exception {
    scan(appConfig);
  }

  /**
   * Try to get bean from Spring container. If the bean is prototype, the container will create a
   * new instance. Else will return the same instance.
   */
  public Object getBean(String name) throws Exception {
    if (beanDefinitionPool.containsKey(name)) {
      var beanDefinition = beanDefinitionPool.get(name);
      if (beanDefinition.getType() == Type.PROTOTYPE) {
        return createBean(beanDefinition);
      } else {
        return objectPool.get(name);
      }
    }
    throw new ClassNotFoundException("Bean " + name + " not found");
  }

  /**
   * Try to get bean from Spring container. If the bean is prototype, the container will create a
   * new instance. Else will return the same instance. If the instance is not "requiredType" or
   * can't do type conversion. IllegalArgumentException will be thrown.
   */
  public <T> T getBean(String name, Class<T> requiredType) throws Exception {
    var obj = getBean(name);
    if (requiredType.isAssignableFrom(obj.getClass())) {
      return (T) obj;
    } else {
      throw new IllegalArgumentException(
          "Can't not cast " + obj.getClass().getName() + " to " + requiredType.getName());
    }
  }

  public Map<String, BeanDefinition> getBeanDefinitionPool() {
    return new HashMap<>(beanDefinitionPool);
  }

  public Map<String, Object> getObjectPool() {
    return new HashMap<>(objectPool);
  }

  /**
   * The function reads "appConfig", scans all the Java files(.class) with @Component annotation,
   * creates beans and bean definitions, saves the beans into objectPool.
   * <p>
   * ObjectPool is a container to help manage beans. IOC: Inversion of Control
   * <p>
   * If appConfig == null. The function will scan all Java files by default and assume there is no
   * bean definition.
   */
  @VisibleForTesting
  void scan(Class<?> appConfig) throws Exception {
    var scanPath = "";
    if (appConfig != null) {
      // read files
      var scanAnnotation = appConfig.getAnnotation(ComponentScan.class);
      scanPath = scanAnnotation == null ? "" : scanAnnotation.value();
      if ("".equals(scanPath)) {
        scanPath = ROOT_PACKAGE;
      }
    } else {
      scanPath = ROOT_PACKAGE;
    }
    var classLoader = Thread.currentThread().getContextClassLoader();
    URL url;
    scanPath = scanPath.replaceAll("\\.", "/");
    url = classLoader.getResource(scanPath);
    if (url == null) {
      throw new IllegalStateException("Package " + scanPath + " doesn't exist");
    }
    var file = new File(url.getPath());
    // result format: List<String>: ["com.toxicaker.abc.UserService", "com.toxicaker.def.AuthService"]
    var javaPaths = listFiles(file).stream().filter(f -> {
      var paths = f.getAbsolutePath().split("\\.");
      var ext = paths[paths.length - 1];
      return "class".equals(ext);
    }).map(f -> {
      var path = f.getAbsolutePath();
      var root = ROOT_PACKAGE.replaceAll("\\.", "/");
      return path.substring(path.indexOf(root), path.indexOf(".class"))
          .replaceAll("/", "\\.");
    }).collect(Collectors.toList());

    // load beans
    for (var classPath : javaPaths) {
      try {
        var clazz = classLoader.loadClass(classPath);
        var componentAnnotation = clazz.getAnnotation(Component.class);
        if (componentAnnotation != null) {
          var beanName = "".equals(componentAnnotation.name()) ? clazz.getName()
              : componentAnnotation.name();
          var beanType = componentAnnotation.type();
          if (beanDefinitionPool.containsKey(beanName)) {
            throw new IllegalStateException("Duplicate bean name " + beanName);
          }
          beanDefinitionPool.put(beanName, new BeanDefinition(clazz, beanName, beanType));
        }
      } catch (ClassNotFoundException e) {
        logger.warn("Class {} not found", classPath, e);
      }
    }
    for (var name : beanDefinitionPool.keySet()) {
      var beanDefinition = beanDefinitionPool.get(name);
      objectPool.put(name, createBean(beanDefinition));
    }
  }

  @VisibleForTesting
  Object createBean(BeanDefinition beanDefinition) throws Exception {
    var clazz = beanDefinition.getClazz();
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      logger.error("Failed to create object for {}", clazz.getName(), e);
      throw e;
    }
  }

  @VisibleForTesting
  List<File> listFiles(File file) {
    if (file == null) {
      return new ArrayList<>();
    }
    var results = new ArrayList<File>();
    if (file.isDirectory()) {
      for (var f : Objects.requireNonNull(file.listFiles())) {
        if (f.isDirectory()) {
          results.addAll(listFiles(f));
        } else {
          results.add(f);
        }
      }
    } else {
      results.add(file);
    }
    return results;
  }

}
