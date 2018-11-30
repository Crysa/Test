package com.common.demo.net.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CharSequenceConverter implements JsonDeserializer<CharSequence> {
  @Override public CharSequence deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return json.getAsString();
  }
}