package com.common.test;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.lib.componet.BaseActivity;
import com.common.lib.router.RouterUtil;
import com.common.lib.utilcode.util.LogUtils;
import com.common.lib.utilcode.util.SizeUtils;
import com.common.lib.vo.TextViewItem;
import com.common.lib.widget.recyclerview.Item;
import com.common.test.config.RoutePath;
import com.common.test.databinding.ActivityMainBinding;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity {

  public ObservableList<Item> data = new ObservableArrayList();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setView(this);
    binding.setLifecycleOwner(this);

    data.add(new TextViewItem("列表").setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        .setHeight(SizeUtils.dp2px(45))
        .setTextSize(SizeUtils.sp2px(16))
        .setListener(v -> {
          RouterUtil.start(RoutePath.ListActivity);
        }));


  }
}
