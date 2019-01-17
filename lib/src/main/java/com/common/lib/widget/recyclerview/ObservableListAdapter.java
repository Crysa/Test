package com.common.lib.widget.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.common.lib.utilcode.util.ObjectUtils;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public class ObservableListAdapter extends RecyclerView.Adapter<BaseBindingHolder> {

  private ObservableList<Item> mItems;


  public ObservableListAdapter() {
    this(new ObservableArrayList<>());
  }


  public ObservableListAdapter(ObservableList<? extends Item> data){
    if (data == null) {
      data = new ObservableArrayList<>();
    }
    setData(data);
  }


  public void setData(ObservableList<? extends Item> data) {
    if (ObjectUtils.isEmpty(data) &&ObjectUtils.isEmpty(this.mItems)) {
      return;
    }
    this.mItems = (ObservableList<Item>) data;
    notifyDataSetChanged();
  }


  @Override public int getItemViewType(int position) {
    return getItem(position).getView();
  }


  @NonNull @Override
  public BaseBindingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    ViewDataBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), viewType, viewGroup,
            false);
    View itemView = binding.getRoot();
    BaseBindingHolder holder =
        new BaseBindingHolder(this, itemView);
    return holder;
  }


  @Override public void onBindViewHolder(@NonNull BaseBindingHolder baseBindingHolder, int position) {
    Item item = getItem(position);
    baseBindingHolder.onBind(item);
    item.onBind(baseBindingHolder);
  }


  private Item getItem(int position) {
    return mItems.get(position);
  }


  @Override public int getItemCount() { return ObjectUtils.isEmpty(mItems)?0:mItems.size(); }


  @Override public void onViewRecycled(@NonNull BaseBindingHolder holder) {
    super.onViewRecycled(holder);
    Item item = ((Item) holder.getItem());
    item.onRecycler(holder);
  }

}
