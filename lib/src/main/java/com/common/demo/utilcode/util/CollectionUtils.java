package com.common.demo.utilcode.util;

import java.util.List;

public class CollectionUtils {
  public static <T> T getOrNUll(List<T> collection, int index) {
    if (collection == null) {
      return null;
    }
    if (collection.size() <= index) {
      return null;
    }
    return collection.get(index);
  }

  public static <T> T getOrDefault(List<T> collection, int index, T object) {
    if (collection == null) {
      return object;
    }
    if (collection.size() <= index) {
      return object;
    }
    return collection.get(index);
  }
}
