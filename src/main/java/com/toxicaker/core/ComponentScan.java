package com.toxicaker.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation defines the scanning path which helps the framework locate the beans
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {

  String value() default "";
}
