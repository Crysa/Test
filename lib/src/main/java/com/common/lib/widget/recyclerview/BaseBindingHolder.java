package com.common.lib.widget.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public class BaseBindingHolder<T extends RecyclerView.Adapter> extends RecyclerView.ViewHolder {
  private T adapter;

  private Item item;

  public Item getItem() { return item; }

  public T getAdapter() { return adapter;}

  public BaseBindingHolder(T adapter, @NonNull View itemView) {
    super(itemView);
    this.adapter = adapter;
  }

  public void onBind(Item item){
    this.item = item;
    ViewDataBinding binding = DataBindingUtil.getBinding(itemView);
    binding.setVariable(item.getVariableId(),item.getItem());
    /**数据改变时立即刷新数据，变化的时间只有一帧*/
    binding.executePendingBindings();
  }

  public void onRecycler(){
    item =null;
  }
}
