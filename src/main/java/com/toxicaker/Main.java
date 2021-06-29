package com.toxicaker;

import java.io.File;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

  public static void main(String[] args) throws Exception {
    var tomcat = new Tomcat();
    tomcat.setPort(Integer.getInteger("port", 9000));
    tomcat.getConnector();
    var ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
    var resources = new StandardRoot(ctx);
    resources.addPreResources(
        new DirResourceSet(resources, "/WEB-INF/classes",
            new File("target/classes").getAbsolutePath(), "/"));
    ctx.setResources(resources);
    tomcat.start();
    tomcat.getServer().await();
  }
}
