package com.common.demo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

/**
 * Created by WhaleFull on 2018/11/30.
 */
public class ContextHelper {
  public static Activity getActivity(View view) {
    if (view == null) {
      return null;
    }

    Context context = view.getContext();
    return getActivity(context);
  }

  public static Activity getActivity(Context context) {
    while (context instanceof ContextWrapper) {
      if (context instanceof Activity) {
        return (Activity) context;
      }
      context = ((ContextWrapper) context).getBaseContext();
    }
    return null;
  }
}
