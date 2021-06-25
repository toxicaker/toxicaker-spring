package com.toxicaker.aop;

import com.toxicaker.core.ApplicationContext;
import com.toxicaker.core.Bean;
import com.toxicaker.core.ComponentScan;
import org.junit.Test;

public class AopBeanPostProcessorTest {

  @Test
  public void testAOP() throws Exception {
    var applicationContext = new ApplicationContext(MyAppConfig.class);
    AopService bean = applicationContext.getBean("com.toxicaker.aop.AopService", AopService.class);
    bean.testMethod();
  }

  @Test
  public void testAOPInterface() throws Exception {
    var applicationContext = new ApplicationContext(MyAppConfig.class);
    AopInterfaceService bean = applicationContext.getBean("com.toxicaker.aop.AopInterfaceServiceImpl", AopInterfaceService.class);
    bean.testInterfaceMethod();
  }

  @ComponentScan("com.toxicaker.aop")
  private static class MyAppConfig {

    @Bean
    Object createBean() {
      return new Object();
    }
  }
}