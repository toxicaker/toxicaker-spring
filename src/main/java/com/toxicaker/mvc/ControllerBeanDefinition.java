package com.toxicaker.mvc;

import com.toxicaker.mvc.RequestMapping.HTTP;
import java.lang.reflect.Method;

public class ControllerBeanDefinition {

  private String url;
  private RequestMapping.HTTP httpMethod;
  private Method method;
  private Object bean;


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HTTP getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(HTTP httpMethod) {
    this.httpMethod = httpMethod;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }
}
