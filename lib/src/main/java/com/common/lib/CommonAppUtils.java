package com.common.lib;

import android.app.Application;
import com.alibaba.android.arouter.launcher.ARouter;

public class CommonAppUtils {

  private static CommonConfig sConfig;

  private static Boolean isDebug = null;

  public static Boolean isDebug() {
    return isDebug != null && isDebug;
  }

  public static void init(CommonConfig config) {
    Application application = config.application;
    sConfig = config;

    initRouter(application);
  }

  private static void initRouter(Application application) {
    if (isDebug()) {
      ARouter.openLog();
      ARouter.openDebug();
    }
    ARouter.init(application);
  }

  public static CommonConfig getConfig() {
    return sConfig;
  }
}
