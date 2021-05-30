package com.toxicaker.core;

import com.toxicaker.example.DbService;
import com.toxicaker.example.UserService;
import com.toxicaker.example.UserServiceImpl;
import com.toxicaker.example.di.positive.A;
import com.toxicaker.example.di.positive.B;
import com.toxicaker.example.di.positive.C;
import com.toxicaker.example.di.positive.D;
import com.toxicaker.example.di.positive.E;
import com.toxicaker.example.di.positive.F;
import com.toxicaker.example.di.positive.G;
import com.toxicaker.unittest.TestFile;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplicationContextTest {


  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testListFilesShouldReturnAllTheFilesWithoutDuplicate() throws Exception {
    var classLoader = Thread.currentThread().getContextClassLoader();
    var url = classLoader.getResource("com/toxicaker/unittest");
    Assert.assertNotNull(url);

    var applicationContext = new ApplicationContext();
    var file = new File(url.getPath());
    var files = applicationContext.listFiles(file);
    Assert.assertEquals(3, files.size());

    var set = new HashSet<String>();
    for (var f : files) {
      set.add(f.getName());
    }
    Assert.assertTrue(set.contains("TestFile1.class"));
    Assert.assertTrue(set.contains("TestFile2.class"));
    Assert.assertTrue(set.contains("TestFile.class"));
  }

  @Test
  public void testListFilesShouldReturnEmptyListWhenParamIsNull() throws Exception {
    var applicationContext = new ApplicationContext();
    var files = applicationContext.listFiles(null);
    Assert.assertEquals(0, files.size());
  }

  @Test
  public void testScanShouldInitBeanPools() throws Exception {
    var applicationContext = new ApplicationContext(MyAppConfig.class);
    var pool = applicationContext.getObjectPool();
    Assert.assertEquals(2, pool.size());
    Assert.assertNotNull(pool.get("com.toxicaker.example.DbService"));
    Assert.assertNotNull(pool.get("com.toxicaker.example.UserServiceImpl"));
    Assert.assertEquals(DbService.class,
        pool.get("com.toxicaker.example.DbService").getClass());
    Assert.assertEquals(UserServiceImpl.class,
        pool.get("com.toxicaker.example.UserServiceImpl").getClass());
  }

  @Test
  public void testScanWithoutAppConfigShouldInitBeanPools() throws Exception {
    var applicationContext = new ApplicationContext();
    var pool = applicationContext.getObjectPool();
    Assert.assertEquals(3, pool.size());
    Assert.assertNotNull(pool.get("com.toxicaker.example.DbService"));
    Assert.assertNotNull(pool.get("com.toxicaker.example.UserServiceImpl"));
    Assert.assertNotNull(pool.get("com.toxicaker.unittest.TestFile"));
    Assert.assertEquals(DbService.class,
        pool.get("com.toxicaker.example.DbService").getClass());
    Assert.assertEquals(UserServiceImpl.class,
        pool.get("com.toxicaker.example.UserServiceImpl").getClass());
    Assert.assertEquals(TestFile.class,
        pool.get("com.toxicaker.unittest.TestFile").getClass());
  }

  @Test
  public void testGetBeanCanGetInterfaceType() throws Exception {
    var applicationContext = new ApplicationContext();
    var bean = applicationContext
        .getBean("com.toxicaker.example.UserServiceImpl", UserService.class);
    Assert.assertNotNull(bean);
    Assert.assertTrue(UserService.class.isAssignableFrom(bean.getClass()));
  }

  @Test
  public void testGetBeanShouldCreateNewObjectIfTypeIsProtoType() throws Exception {
    var applicationContext = new ApplicationContext();
    var bean1 = applicationContext
        .getBean("com.toxicaker.example.DbService");
    var bean2 = applicationContext
        .getBean("com.toxicaker.example.DbService");
    Assert.assertNotNull(bean1);
    Assert.assertNotNull(bean2);
    Assert.assertNotEquals(bean1, bean2);
  }

  @Test
  public void testGetBeanShouldNotCreateNewObjectIfTypeIsSingleton() throws Exception {
    var applicationContext = new ApplicationContext();
    var bean1 = applicationContext
        .getBean("com.toxicaker.example.UserServiceImpl");
    var bean2 = applicationContext
        .getBean("com.toxicaker.example.UserServiceImpl");
    Assert.assertNotNull(bean1);
    Assert.assertNotNull(bean2);
    Assert.assertEquals(bean1, bean2);
  }

  @Test
  public void testCheckCircularReferencePositiveTest1() throws Exception {
    Map<String, Set<String>> graph = Map.of("A", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testCheckCircularReferencePositiveTest2() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D",
            new HashSet<>(),
            "E", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testCheckCircularReferencePositiveTest3() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D",
            new HashSet<>(),
            "E", Set.of("C", "D"));
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testCheckCircularReferencePositiveTest4() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D",
            new HashSet<>(),
            "E", Set.of("C", "D"), "F", Set.of("G"), "G", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckCircularReferenceNegativeTest1() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D", Set.of("A"),
            "E", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckCircularReferenceNegativeTest2() throws Exception {
    Map<String, Set<String>> graph = Map.of("A", Set.of("A"));
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckCircularReferenceNegativeTest3() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D", Set.of("A"),
            "E", Set.of("C", "D"), "F", Set.of("G"), "G", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testDependencyInjectionPositive() throws Exception {
    var applicationContext = new ApplicationContext(DIPositiveConfig.class);
    A a = applicationContext.getBean("com.toxicaker.example.di.positive.A",
        com.toxicaker.example.di.positive.A.class);
    B b = applicationContext
        .getBean("com.toxicaker.example.di.positive.B", com.toxicaker.example.di.positive.B.class);
    C c = applicationContext
        .getBean("com.toxicaker.example.di.positive.C", com.toxicaker.example.di.positive.C.class);
    D d = applicationContext
        .getBean("com.toxicaker.example.di.positive.D", com.toxicaker.example.di.positive.D.class);
    E e = applicationContext
        .getBean("com.toxicaker.example.di.positive.E", com.toxicaker.example.di.positive.E.class);
    F f = applicationContext
        .getBean("com.toxicaker.example.di.positive.F", com.toxicaker.example.di.positive.F.class);
    G g = applicationContext
        .getBean("com.toxicaker.example.di.positive.G", com.toxicaker.example.di.positive.G.class);

    Assert.assertNotNull(a);
    Assert.assertNotNull(a.getB());
    Assert.assertNotNull(a.getC());

    Assert.assertNotNull(b);
    Assert.assertNotNull(b.getD());
    Assert.assertNotNull(b.getE());

    Assert.assertNotNull(c);
    Assert.assertNotNull(d);

    Assert.assertNotNull(e);
    Assert.assertNotNull(e.getD());
    Assert.assertNotNull(e.getC());

    Assert.assertNotEquals(b, a.getB());
    Assert.assertEquals(c, a.getC());
    Assert.assertEquals(d, b.getD());
    Assert.assertEquals(e, b.getE());
    Assert.assertEquals(d, e.getD());
    Assert.assertEquals(c, e.getC());

    Assert.assertNotNull(f);
    Assert.assertNotNull(g);
    Assert.assertEquals(g, f.getG());
  }

  @Test(expected = IllegalStateException.class)
  public void testDependencyInjectionNegative() throws Exception {
    var applicationContext = new ApplicationContext(DINegativeConfig.class);
  }

  @Test(expected = IllegalStateException.class)
  public void testInjectObjectWhichIsNotBeanShouldThrowException() throws Exception {
    var applicationContext = new ApplicationContext(BadInjectionConfig.class);
  }


  @ComponentScan("com.toxicaker.example")
  private static class MyAppConfig {

    @Bean
    Object createBean() {
      return new Object();
    }
  }

  @ComponentScan("com.toxicaker.example.di.positive")
  private static class DIPositiveConfig {


  }

  @ComponentScan("com.toxicaker.example.di.negative")
  private static class DINegativeConfig {


  }

  @ComponentScan("com.toxicaker.example.di.badInjection")
  private static class BadInjectionConfig {


  }
}