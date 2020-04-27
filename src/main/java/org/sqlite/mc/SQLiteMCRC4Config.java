package org.sqlite.mc;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteMCRC4Config extends SQLiteMCConfig.Builder {

  public SQLiteMCRC4Config(){
    super();
    setCipher(CipherAlgorithm.RC4);
  }

  public SQLiteMCRC4Config setLegacyValue(int value){
    assert isValid(value, 1,1);
    super.setLegacy(value);
    return this;
  }

  public SQLiteMCRC4Config setlegacyPageSize(int value){
    assert isValid(value, 0,65536);
    super.setLegacyPageSize(value);
    return this;
  }


  public static SQLiteMCRC4Config getDefault() {
    SQLiteMCRC4Config config = new SQLiteMCRC4Config();

    config.setLegacyValue(1);
    config.setlegacyPageSize(0);

    return config;
  }
}
