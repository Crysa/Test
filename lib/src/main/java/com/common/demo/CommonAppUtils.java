package com.common.demo;

public class CommonAppUtils {

  private static CommonConfig sConfig;

  private static Boolean isDebug = null;

  public static Boolean isDebug() {
    return isDebug !=null && isDebug;
  }

  public static void init(CommonConfig config){
    sConfig=config;


  }

  public static CommonConfig getConfig() {
    return sConfig;
  }
}
