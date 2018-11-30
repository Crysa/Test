package com.common.demo.net.converter;

import com.common.demo.net.BaseResponse;
import com.common.demo.utilcode.util.ObjectUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by WhaleFull on 2018/11/30.
 * des: BaseResponse 基本数据类型的自定义转换类，
 * 解决问题为 data 类型错误的转换问题，
 * 正确情况下
 * data:{
 *
 * }
 * 错误情况下
 * data:""
 */
public class BaseResponseConverter<T> implements JsonDeserializer<BaseResponse<T>> {
  @Override public BaseResponse deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    JsonElement jsonCode = jsonObject.get(BaseResponse.CODE);
    JsonElement jsonMsg = jsonObject.get(BaseResponse.MSG);
    JsonElement jsonData = jsonObject.get(BaseResponse.DATA);
    T value;
    if (ObjectUtils.isNotEmpty(jsonData) && jsonData.isJsonObject()) {
      value = context.deserialize(jsonData, ((ParameterizedType) typeOfT).getActualTypeArguments()[0]);
    }else {
      value = null;
    }
    BaseResponse baseResponse = new BaseResponse();
    baseResponse.setCode(jsonCode.getAsInt());
    baseResponse.setMsg(jsonMsg.getAsString());
    baseResponse.setData(value);
    return baseResponse;
  }
}
