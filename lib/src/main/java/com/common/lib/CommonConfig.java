package com.common.lib;

import android.app.Application;

/**
 * Created by WhaleFull on 2018/11/30.
 * des：总配置
 */
public class CommonConfig {
  public Application application;
  public String scheme = "http";
  /**
   * 域名
   */
  public String host;
  /**
   * 端口
   */
  public String port;

  /**
   * api 域
   */
  public String api;

  /**
   * Android N 以上 的文件分享所需授权
   */
  public String authority;


  public CommonConfig(Application application, String host, String port, String api) {
    this.application = application;
    this.host = host;
    this.port = port;
    this.api = api;
  }

  public CommonConfig(Application application, String scheme, String host, String port,
      String api, String authority) {
    this.application = application;
    this.scheme = scheme;
    this.host = host;
    this.port = port;
    this.api = api;
    this.authority = authority;
  }
}
