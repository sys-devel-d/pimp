package com.pimp.commons.mongo;

import java.io.Serializable;

public interface IKeyedObject extends Serializable {
  String getKey();

  void setKey(String key);
}
