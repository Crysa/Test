package com.common.lib.databinding;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.common.lib.utilcode.util.ObjectUtils;
import com.common.lib.widget.recyclerview.ObservableListAdapter;
import com.common.lib.widget.recyclerview.OnItemClickListener;

/**
 * Created by WhaleFull on 2018/11/28.
 */
public class RecyclerDataBinding {

  @BindingAdapter(value = {
      "recyclerData", "itemClick", "layoutManager", "orientation", "customerLayoutManager", "span",
      "prefetchCount"
  }, requireAll = false)
  public static void bindingRecyclerAdapter(RecyclerView view, ObservableList data,
      OnItemClickListener listener, String layoutManager, int orientation,
      boolean customerLayoutManager, int span, int prefetchCount) {
    if (!customerLayoutManager) {
      RecyclerView.LayoutManager manager = view.getLayoutManager();
      if (span > 0) {
        layoutManager = "GridLayoutManager";
      } else if (ObjectUtils.isEmpty(layoutManager)) {
        layoutManager = "LinearLayoutManager";
      }
      switch (layoutManager) {
        case "GridLayoutManager":
          if (manager != null && manager.getClass() == GridLayoutManager.class) {
            manager.setItemPrefetchEnabled(true);
            ((GridLayoutManager) manager).setInitialPrefetchItemCount(prefetchCount);
            ((GridLayoutManager) manager).setSpanCount(span);
            ((GridLayoutManager) manager).setOrientation(orientation == 0 ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL);
          } else {
            manager = new GridLayoutManager(view.getContext(), span,
                orientation == 0 ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL,
                false);
            //设置LayoutManager.setItemPrefetchEnabled()来对比开启和关闭预取功能
            manager.setItemPrefetchEnabled(true);
            //这个方法返回的是当前位置的 item 跨度大小。
            //((GridLayoutManager) manager).setSpanSizeLookup();
            ((GridLayoutManager) manager).setInitialPrefetchItemCount(prefetchCount);
            view.setLayoutManager(manager);
          }
        case "LinearLayoutManager":
          default:
            if (manager!=null && manager.getClass() == LinearLayoutManager.class){
              ((LinearLayoutManager) manager).setOrientation(orientation == 0 ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
              manager.setItemPrefetchEnabled(true);
              ((LinearLayoutManager) manager).setInitialPrefetchItemCount(prefetchCount);
            }else {
              manager = new LinearLayoutManager(view.getContext(),orientation == 0 ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL,false);
              manager.setItemPrefetchEnabled(true);
              ((LinearLayoutManager) manager).setInitialPrefetchItemCount(prefetchCount);
              view.setLayoutManager(manager);
            }
      }
    }
    if (view.getAdapter() == null) {
      ObservableListAdapter adapter = new ObservableListAdapter(data);
      view.setAdapter(adapter);
    }else {
      ObservableListAdapter adapter = (ObservableListAdapter) view.getAdapter();
      adapter.setData(data);
    }
  }
}
