package com.toxicaker.core;

import com.toxicaker.example.DbService;
import com.toxicaker.example.UserService;
import com.toxicaker.example.UserServiceImpl;
import com.toxicaker.unittest.TestFile;
import java.io.File;
import java.util.HashMap;
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
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D", new HashSet<>(),
            "E", new HashSet<>());
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testCheckCircularReferencePositiveTest3() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D", new HashSet<>(),
            "E", Set.of("C", "D"));
    var applicationContext = new ApplicationContext();
    applicationContext.checkCircularReference(graph);
  }

  @Test
  public void testCheckCircularReferencePositiveTest4() throws Exception {
    Map<String, Set<String>> graph = Map
        .of("A", Set.of("B", "C"), "B", Set.of("D", "E"), "C", new HashSet<>(), "D", new HashSet<>(),
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

  @ComponentScan("com.toxicaker.example")
  private static class MyAppConfig {

  }
}