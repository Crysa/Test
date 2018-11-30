package com.common.demo.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WhaleFull on 2018/11/30.
 * des:APP 与后台交互的基础数据类型,根据实际动态配置
 */
public class BaseResponse<T> {
  public static final Integer CODE_SUCCESS = 0;
  public static final String CODE = "error_code";
  public static final String MSG = "error_msg";
  public static final String DATA = "data";

  @SerializedName("error_code")
  private int code;

  @SerializedName("error_msg")
  private String msg;

  private T data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) { this.code = code; }

  public String getMsg() { return msg; }

  public void setMsg(String msg) { this.msg = msg; }

  public T getData() { return data; }

  public void setData(T data) { this.data = data; }
}
