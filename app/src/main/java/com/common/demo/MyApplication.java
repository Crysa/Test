package com.common.demo;

/**
 * Created by WhaleFull on 2018/11/30.
 */
public class MyApplication extends CommonAppliction {
  @Override public void onCreate() {
    super.onCreate();
    CommonAppUtils.init(new CommonConfig(this,"","",""));
  }
}
