package com.toxicaker.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is the marker to mark which class is application configuration
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AppConfig {

}
