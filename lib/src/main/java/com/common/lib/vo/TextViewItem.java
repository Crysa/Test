package com.common.lib.vo;

import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.common.lib.R;
import com.common.lib.utilcode.util.ObjectUtils;
import com.common.lib.widget.recyclerview.Item;

/**
 * Created by WhaleFull on 2019/1/16.
 */
public class TextViewItem implements Item {
  public ObservableField<CharSequence> text = new ObservableField<>();
  public View.OnClickListener listener;
  public int width = ViewGroup.LayoutParams.WRAP_CONTENT;
  public int height = ViewGroup.LayoutParams.WRAP_CONTENT;
  public int background = Color.TRANSPARENT;
  public int textSize = 20;
  public int textColor = Color.BLACK;
  public int gravity = Gravity.CENTER;

  public TextViewItem(CharSequence text) {
    this.text.set(text);
  }

  @Override public int getView() {
    return R.layout.item_text;
  }

  public void onClick(View view) {
    if (listener!=null) {
      listener.onClick(view);
    }
  }

  public TextViewItem setListener(View.OnClickListener listener) {
    this.listener = listener;
    return this;
  }

  public TextViewItem setBackground(int background) {
    this.background = background;
    return this;
  }

  public TextViewItem setHeight(int height) {
    this.height = height;
    return this;
  }

  public TextViewItem setWidth(int width) {
    this.width = width;
    return this;
  }

  public TextViewItem setText(CharSequence text) {
    this.text.set(text);
    return this;
  }

  public TextViewItem setTextColor(int textColor) {
    this.textColor = textColor;
    return this;
  }

  public TextViewItem setTextSize(int textSize) {
    this.textSize = textSize;
    return this;
  }

  public TextViewItem setGravity(int gravity) {
    this.gravity = gravity;
    return this;
  }
}
