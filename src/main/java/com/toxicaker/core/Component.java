package com.toxicaker.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The marker of Java beans. The bean will be managed by Spring context or Spring container. There
 * are two types: 1. PROTOTYPE: Create the new object once gets called. 2. SINGLETON: Always return
 * the same object when gets called.
 * <pre>
 * @Component
 * public class UserService {
 *
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

  Type type() default Type.SINGLETON;

  String name() default "";

  enum Type {
    PROTOTYPE,
    SINGLETON
  }
}
