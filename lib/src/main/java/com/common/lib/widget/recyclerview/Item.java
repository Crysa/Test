package com.common.lib.widget.recyclerview;

import com.common.lib.BR;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public interface Item {

  /** 返回视图 */
  int getView();

  /**
   * 用于 GridLayoutManager 的 span 值，默认为-1
   * 返回 -1 则设置全屏
   * 按需 覆写
   */
  default int getGridSpan() {
    return -1;
  }

  default void onCreate(BaseBindingHolder holder) {

  }

  default void onBind(BaseBindingHolder holder) {

  }

  default void onRecycler(BaseBindingHolder holder) {

  }

  /**所有ID */
  default int getVariableId() {
    return BR.item;
  }

  default <T extends Item> T getItem() {
    return (T) this;
  }
}
