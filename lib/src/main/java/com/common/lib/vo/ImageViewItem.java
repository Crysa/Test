package com.common.lib.vo;

import com.common.lib.R;
import com.common.lib.widget.recyclerview.Item;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public class ImageViewItem implements Item {
  @Override public int getView() {
    return R.layout.item_image;
  }
}
