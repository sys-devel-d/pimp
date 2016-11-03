package com.pimp.commons;

import java.io.InputStream;

/**
 * @author Kevin Goy
 */
public interface IFileStorage {
  void write(String var1, InputStream var2);

  InputStream read(String var1);

  void delete(String var1);
}
