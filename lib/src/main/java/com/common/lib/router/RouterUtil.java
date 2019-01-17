package com.common.lib.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.lib.BR;
import com.common.lib.CommonAppUtils;
import com.common.lib.utilcode.util.LogUtils;
import com.common.lib.utilcode.util.NetworkUtils;
import com.common.lib.utilcode.util.ObjectUtils;
import com.common.lib.utilcode.util.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.http.PATCH;

/**
 * Created by WhaleFull on 2019/1/17.
 * 路由跳转
 */
public class RouterUtil {
  public static final String SPIT = "?";
  public static final String SPACILESPIT = "<aix>";
  public static final int DEFAULT_CODE = -1;
  private static final Pattern p = java.util.regex.Pattern.compile("(\\?|&+)(.+?)=([^&|?]*)");

  // <editor-fold defaultstate="collapsed" desc="startActivity No param">
  public static void start(String activityPath) {
    start(activityPath, null, DEFAULT_CODE, null);
  }

  public static void start(String activityPath, Activity activity, int requestCode) {
    start(activityPath, activity, requestCode, null);
  }

  public static void start(String activityPath, Activity activity,
      NavigationCallback navigationCallback) {
    start(activityPath, activity, navigationCallback);
  }

  public static void start(String activityPath, Activity activity, int requestCode,
      NavigationCallback callback) {
    if (CommonAppUtils.isDebug()) {
      LogUtils.e(activityPath);
    }
    if (ObjectUtils.isEmpty(activityPath)) {
      LogUtils.e("Router: activityPath 为 null");
      return;
    }
    Postcard build = ARouter.getInstance().build(activityPath);
    if (activity == null) {
      build.navigation(Utils.getTopActivityOrApp());
    } else {
      ARouter.getInstance().build(activityPath).navigation(activity, requestCode, callback);
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="startActivity  param">
  /**
   * url 带参数的方法，注意 必须使用 a= i 192 & b= s 字符串 这样的方式，在 value 前带一个类型的 key
   */
  public static void startWith(String activityPath) {
    start(activityPath, null, DEFAULT_CODE, null);
  }

  public static void startWith(String activityPath, Activity activity, int requestCode) {
    start(activityPath, activity, requestCode, null);
  }

  public static void startWith(String activityPath, Activity activity,
      NavigationCallback navigationCallback) {
    start(activityPath, activity, navigationCallback);
  }

  public static void startWith(String activityPath, Activity activity, int requestCode,
      NavigationCallback callback) {
    if (CommonAppUtils.isDebug()) {
      LogUtils.e(activityPath);
    }
    if (ObjectUtils.isEmpty(activityPath)) {
      LogUtils.e("Router: activityPath 为 null");
      return;
    }
    Postcard build = handleParams(activityPath);
    if (activity == null) {
      build.navigation(Utils.getTopActivityOrApp());
    } else {
      ARouter.getInstance().build(activityPath).navigation(activity, requestCode, callback);
    }
  }
  // </editor-fold>


  // <editor-fold defaultstate="collapsed" desc="createFragment No Param"> 
  public static BaseFragment createFragment(String path) {
    if (CommonAppUtils.isDebug()) {
      LogUtils.e(path);
    }
    if (ObjectUtils.isEmpty(path)) {
      LogUtils.e("start: 空的 Fragment Path");
      return null;
    }
    return (BaseFragment) ARouter.getInstance()
        .build(path)
        .navigation();
  }
  // </editor-fold> 


 // <editor-fold defaultstate="collapsed" desc="createFragment Param">
  /**
   * url 带参数的方法，注意 必须使用 a=i192 这样的方式，在 value 前带一个类型的 key
   */
  public static BaseFragment createFragmentWith(String path) {
    if (CommonAppUtils.isDebug()) {
      LogUtils.e(path);
    }
    if (ObjectUtils.isEmpty(path)) {
      LogUtils.e("start: 空的 Fragment Path");
      return null;
    }
    if (!path.contains("?") && !path.contains("&")) {
      LogUtils.e("createFragmentWith: " + path + "没有携带参数");
      return (BaseFragment) ARouter.getInstance()
          .build(path)
          .navigation();
    }
    Postcard build = handleParams(path);
    return (BaseFragment) build.navigation();
  }
 // </editor-fold> 
  
  

  @NonNull private static Postcard handleParams(String path) {
    String activityPath = path.substring(0, path.indexOf("?"));
    Postcard build = ARouter.getInstance().build(activityPath);
    Matcher matcher = p.matcher(path);
    while (matcher.find()){
      String key = matcher.group(2);
      String s = matcher.group(3);
      if (s.length()==1){
        continue;
      }
      String value = s.substring(1, s.length());
      switch (value.charAt(0)){
        case 'i':
          build.withInt(key,Integer.valueOf(value));
          break;
        case 's':
          build.withString(key,value.replace(SPACILESPIT,SPIT));
          break;
        case 'l':
          build.withLong(key, Long.valueOf(value));
          break;
        case 'b':
          build.withBoolean(key, Boolean.valueOf(value));
          break;
        case 'f':
          build.withFloat(key, Float.valueOf(value));
          break;
        case 'd':
          build.withDouble(key, Double.valueOf(value));
          break;
        default:
      }
    }
    return build;
  }
}
