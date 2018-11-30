package com.common.demo;

import android.app.Application;

public class CommonAppliction extends Application {
  @Override public void onCreate() {
    super.onCreate();
    
    initCommon();
  }

  private void initCommon() {
    //CommonAppUtils.init();
  }
}
