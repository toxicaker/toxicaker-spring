package com.toxicaker.exmaple;

import com.toxicaker.mvc.Controller;
import com.toxicaker.mvc.RequestMapping;
import com.toxicaker.mvc.RequestMapping.HTTP;
import com.toxicaker.mvc.RequestParam;

@Controller("/user")
public class MyController {

  @RequestMapping(value = "/abc", method = HTTP.GET)
  public TestJsonObject getUser(
      @RequestParam(value = "userId", required = false, defaultValue = "hello") String userId) {
    return new TestJsonObject("userId: " + userId);
  }
}
