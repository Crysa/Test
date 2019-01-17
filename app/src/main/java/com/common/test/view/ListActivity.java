package com.common.test.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.lib.componet.BaseActivity;
import com.common.test.R;
import com.common.test.config.RoutePath;
import com.common.test.databinding.ActivityListBinding;
import com.common.test.viewmodel.ListViewModel;

/**
 * Created by WhaleFull on 2019/1/16.
 */
@Route(path = RoutePath.ListActivity)
public class ListActivity extends BaseActivity {
  ActivityListBinding binding;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
    ListViewModel viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
    binding.setViewModel(viewModel);
  }
}
