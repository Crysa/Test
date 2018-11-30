package com.common.demo.net.converter;

import com.common.demo.utilcode.util.ObjectUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class FloatConverter implements JsonDeserializer<Float> {

  @Override
  public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String stringValue = json.getAsString();
    if (ObjectUtils.isEmpty(stringValue)) {
      return null;
    }
    try {
      return Float.valueOf(stringValue);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}