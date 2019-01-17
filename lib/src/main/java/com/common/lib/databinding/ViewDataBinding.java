package com.common.lib.databinding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.common.lib.utilcode.util.ObjectUtils;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public class ViewDataBinding {
  @BindingAdapter("bindWidth")
  public static void onBindViewWidth(View view,int width){
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (ObjectUtils.isNotEmpty(params)) {
      if (params.width ==width){
        return;
      }
      params.width = width;
      view.requestLayout();
    }
  }

  @BindingAdapter("bindHeight")
  public static void onBindViewHeight(View view,int height){
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (ObjectUtils.isNotEmpty(params)) {
      if (params.height ==height){
        return;
      }
      params.height = height;
      view.requestLayout();
    }
  }
}
