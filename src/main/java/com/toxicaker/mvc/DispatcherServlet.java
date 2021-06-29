package com.toxicaker.mvc;

import com.alibaba.fastjson.JSON;
import com.toxicaker.core.ApplicationContext;
import com.toxicaker.mvc.RequestMapping.HTTP;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {

  private Map<String, Map<HTTP, ControllerBeanDefinition>> mapping = new HashMap<>();

  @Override
  public void init() {
    try {
      var applicationContext = new ApplicationContext();
      mapping = applicationContext.getControllerMappings();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    try {
      this.dispatch(req, resp);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      this.dispatch(req, resp);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    try {
      this.dispatch(req, resp);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    try {
      this.dispatch(req, resp);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    var reqUrl = req.getRequestURI();
    var reqMethod = req.getMethod();
    ControllerBeanDefinition controllerBeanDefinition = null;
    switch (reqMethod) {
      case "GET":
        controllerBeanDefinition = mapping.getOrDefault(reqUrl, new HashMap<>()).get(HTTP.GET);
        break;
      case "POST":
        controllerBeanDefinition = mapping.getOrDefault(reqUrl, new HashMap<>()).get(HTTP.POST);
        break;
      case "PUT":
        controllerBeanDefinition = mapping.getOrDefault(reqUrl, new HashMap<>()).get(HTTP.PUT);
        break;
      case "DELETE":
        controllerBeanDefinition = mapping.getOrDefault(reqUrl, new HashMap<>()).get(HTTP.DELETE);
        break;
    }
    String output = reqMethod + ":" + reqUrl + " not found.";
    if (controllerBeanDefinition != null) {
      Object[] args;
      try {
        args = checkRequestParam(controllerBeanDefinition, req);
      } catch (IllegalArgumentException ex) {
        var pw = resp.getWriter();
        pw.write(ex.getMessage());
        pw.flush();
        return;
      }
      var result = controllerBeanDefinition.getMethod()
          .invoke(controllerBeanDefinition.getBean(), args);
      if (result instanceof String) {
        output = (String) result;
      } else {
        output = JSON.toJSONString(result);
      }
    }
    var pw = resp.getWriter();
    pw.write(output);
    pw.flush();
  }

  private Object[] checkRequestParam(ControllerBeanDefinition controllerBeanDefinition,
      HttpServletRequest req) {
    var method = controllerBeanDefinition.getMethod();
    Object[] args = new String[method.getParameters().length];

    for (int i = 0; i < method.getParameters().length; i++) {
      var param = method.getParameters()[i];
      var requestParam = param.getAnnotation(RequestParam.class);
      if (requestParam != null) {
        String paramName = requestParam.value();
        var val = req.getParameter(paramName);
        if (requestParam.required()) {
          if (val == null) {
            throw new IllegalArgumentException("Missing parameter " + paramName);
          } else {
            args[i] = val;
          }
        } else {
          if (val == null) {
            args[i] = requestParam.defaultValue();
          } else {
            args[i] = val;
          }
        }
      } else {
        args[i] = null;
      }
    }
    return args;
  }
}
