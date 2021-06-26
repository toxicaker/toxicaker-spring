package com.toxicaker.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

  String value() default "";

  HTTP method() default HTTP.GET;

  enum HTTP {
    GET,
    POST,
    PUT,
    DELETE
  }
}
