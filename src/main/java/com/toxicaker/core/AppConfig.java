package com.toxicaker.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is the marker to mark which class is application configuration. Usage:
 * <pre>
 *   @ComponentScan("com.toxicaker.example")
 *   private static class MyAppConfig {
 *
 *     @Bean
 *     Object createBean (){
 *       return new Object();
 *     }
 *   }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AppConfig {

}
