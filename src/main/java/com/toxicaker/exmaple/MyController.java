package com.toxicaker.exmaple;

import com.toxicaker.mvc.Controller;
import com.toxicaker.mvc.RequestMapping;
import com.toxicaker.mvc.RequestMapping.HTTP;

@Controller("/user")
public class MyController {

  @RequestMapping(value = "/abc", method = HTTP.GET)
  public TestJsonObject getUser() {
    return new TestJsonObject("userId: " + "abc");
  }
}
