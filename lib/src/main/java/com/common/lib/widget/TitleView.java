package com.common.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.lib.R;
import com.common.lib.utilcode.util.ResourcesUtils;

/**
 * Created by WhaleFull on 2019/1/17.
 */
public class TitleView extends FrameLayout {
  private TextView mTitle;
  private ImageView mLeftImageView, mRightImageView;
  private TextView mLeftTextView, mRightTextView;

  private String mTitleText;
  private int mTitleColor;
  private int mTitleSize;

  @DrawableRes
  private int mLeftRes, mRightRes;
  private String mLeftText, mRightText;
  private int mLeftTextColor, mRightTextColor;
  private int mLeftTextSize, mRightTextSize;
  private int mLeftWidth, mLeftHeight;
  private int mRightWidth, mRightHeight;
  private boolean mLeftShow, mRightShow;
  private int mViewPadding;
  private int mLeftId, mRightId;
  private int mClickArea;
  private String mLeftHandlerName, mRightHandlerName;
  private OnClickListener mOnLeftClickListener, mOnRightClickListener;
  private Paint mPaint;
  private int mDividerColor;
  private int mDividerHeight;

  public TitleView(Context context) {
    this(context, null);
  }

  public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context, attrs, defStyleAttr);
  }

  private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
    TypedArray typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, defStyleAttr);

    //所有子控件的padding间距
    mViewPadding = typedArray.getDimensionPixelSize(R.styleable.TitleView_viewPadding, 0);
    //点击范围
    mClickArea = typedArray.getDimensionPixelSize(R.styleable.TitleView_clickArea, 100);
    //标题属性
    mTitleText = typedArray.getString(R.styleable.TitleView_titleText);
    mTitleColor = typedArray.getColor(R.styleable.TitleView_titleColor, Color.BLACK);
    mTitleSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_titleSize, 20);
    //左边按钮属性
    mLeftId = typedArray.getResourceId(R.styleable.TitleView_leftId, NO_ID);
    mLeftRes = typedArray.getResourceId(R.styleable.TitleView_leftImage, 0);
    mLeftText = typedArray.getString(R.styleable.TitleView_leftText);
    mLeftTextColor = typedArray.getColor(R.styleable.TitleView_leftColor, Color.WHITE);
    mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_leftSize, 18);
    mLeftWidth = typedArray.getDimensionPixelSize(R.styleable.TitleView_leftWidth,
        LayoutParams.WRAP_CONTENT);
    mLeftHeight = typedArray.getDimensionPixelSize(R.styleable.TitleView_leftHeight,
        LayoutParams.MATCH_PARENT);
    // 默认显示左边按钮
    mLeftShow = typedArray.getBoolean(R.styleable.TitleView_showLeft, true);
    mLeftHandlerName = typedArray.getString(R.styleable.TitleView_leftOnClick);
    //右边按钮属性
    mRightId = typedArray.getResourceId(R.styleable.TitleView_rightId, NO_ID);
    mRightRes = typedArray.getResourceId(R.styleable.TitleView_rightImage, 0);
    mRightText = typedArray.getString(R.styleable.TitleView_rightText);
    mRightTextColor = typedArray.getColor(R.styleable.TitleView_rightColor, Color.WHITE);
    mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_rightSize, 18);
    mRightWidth = typedArray.getDimensionPixelSize(R.styleable.TitleView_rightWidth,
        LayoutParams.WRAP_CONTENT);
    mRightHeight = typedArray.getDimensionPixelSize(R.styleable.TitleView_rightHeight,
        LayoutParams.MATCH_PARENT);
    // 默认不显示右边按钮
    mRightShow = typedArray.getBoolean(R.styleable.TitleView_showRight, false);
    mRightHandlerName = typedArray.getString(R.styleable.TitleView_rightOnClick);
    mDividerColor = typedArray.getColor(R.styleable.TitleView_titleDividerColor, 0);
    mDividerHeight = typedArray.getDimensionPixelSize(R.styleable.TitleView_titleDividerHeight, 0);

    typedArray.recycle();
    //设置属性
    setTitle(mTitleText);
    if (mLeftShow) {
      addView(1);
    }
    if (mRightShow) {
      addView(2);
    }
  }

  public void setTitle(String text) {
    if (mTitle == null) {
      mTitle = new TextView(this.getContext());
      LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
      params.gravity = Gravity.CENTER;
      mTitle.setLayoutParams(params);
      mTitle.setTextColor(mTitleColor);
      mTitle.setGravity(Gravity.CENTER);
      mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
      addView(mTitle);
    }
    mTitle.setText(text);
  }

  /**
   * 1添加左边  2 添加右边
   */
  private void addView(int i) {
    View view;
    if (i == 1 && mLeftRes == 0) {
      mLeftTextView = new TextView(this.getContext());
      mLeftTextView.setGravity(Gravity.CENTER);
      mLeftTextView.setText(mLeftText);
      mLeftTextView.setTextSize(mLeftTextSize);
      mLeftTextView.setTextColor(mLeftTextColor);
      //view = view;
    } else {

    }
  }
}
