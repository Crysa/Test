package com.common.lib;

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
