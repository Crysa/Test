package com.common.demo.net.converter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalConverter extends TypeAdapter<BigDecimal> {

  @Override public void write(JsonWriter out, BigDecimal value) throws IOException {
    out.value(value);
  }

  @Override public BigDecimal read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    try {
      String string = in.nextString();
      if (string.length() > 0) {
        return new BigDecimal(string);
      }
      return null;
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(e);
    }
  }
}