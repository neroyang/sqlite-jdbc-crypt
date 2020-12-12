package org.sqlite.mc;

import org.sqlite.SQLiteConfig;

public class SQLiteMCWxAES256Config extends SQLiteMCConfig.Builder {

  public SQLiteMCWxAES256Config(){
    super();
    setCipher(CipherAlgorithm.WX_AES256);
  }

  @Override
  public SQLiteMCWxAES256Config setLegacy(int value) {
    assert isValid(value, 0,1);
    super.setLegacy(value);
    return this;
  }

  @Override
  public SQLiteMCWxAES256Config setLegacyPageSize(int value) {
    assert isValid(value,0,65536);
    super.setLegacyPageSize(value);
    return this;
  }

  @Override
  public SQLiteMCWxAES256Config setKdfIter(int value) {
    assert isValid(value, 1, Integer.MAX_VALUE);
    super.setKdfIter(value);
    return this;
  }

  public static SQLiteMCWxAES256Config getDefault(){
    SQLiteMCWxAES256Config config = new SQLiteMCWxAES256Config();
    config.setLegacy(0);
    config.setLegacyPageSize(0);
    config.setKdfIter(4001);
    return config;
  }


}
