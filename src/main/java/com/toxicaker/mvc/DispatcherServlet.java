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
      var result = controllerBeanDefinition.getMethod().invoke(controllerBeanDefinition.getBean());
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
}
