package org.sqlite.mc;

import org.sqlite.SQLiteConfig;

public class SQLiteMCSqlCipherConfig extends SQLiteMCConfig.Builder {

  public SQLiteMCSqlCipherConfig() {
    super();
    setCipher(CipherAlgorithm.SQL_CIPHER);
  }

  public SQLiteMCSqlCipherConfig setLegacy(int value) {
    assert isValid(value, 0, 4);
    super.setLegacy(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setLegacyPageSize(int value) {
    assert isValid(value, 0, 65536);
    super.setLegacyPageSize(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setKdfIter(int value) {
    assert isValid(value, 1, Integer.MAX_VALUE);
    super.setKdfIter(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setFastKdfIter(int value) {
    assert isValid(value, 1, Integer.MAX_VALUE);
    super.setFastKdfIter(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setHmacUse(boolean value) {
    super.setHmacUse(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setHmacPgno(HmacPgno value) {
    assert isValid(value.ordinal(), 0, 2);
    super.setHmacPgno(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setHmacSaltMask(int value) {
    assert isValid(value, 0, 255);
    super.setHmacSaltMask(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setKdfAlgorithm(KdfAlgorithm value) {
    assert isValid(value.ordinal(), 0, 2);
    super.setKdfAlgorithm(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setHmacAlgorithm(HmacAlgorithm value) {
    assert isValid(value.ordinal(), 0, 2);
    super.setHmacAlgorithm(value);
    return this;
  }

  public SQLiteMCSqlCipherConfig setPlaintextHeaderSize(int value) {
    assert isValid(value, 0, 100);
    assert value % 16 == 0; //Must be multiple of 16
    super.setPlaintextHeaderSize(value);
    return this;
  }

  public static SQLiteMCSqlCipherConfig getDefault() {
    return getV4Defaults();
  }

  public static SQLiteMCSqlCipherConfig getV1Defaults() {
    SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
    config.setKdfIter(4000);
    config.setFastKdfIter(2);
    config.setHmacUse(false);
    config.setLegacy(1);
    config.setLegacyPageSize(1024);
    config.setKdfAlgorithm(KdfAlgorithm.SHA1);
    config.setHmacAlgorithm(HmacAlgorithm.SHA1);
    return  config;
  }

  public static SQLiteMCSqlCipherConfig getV2Defaults() {
    SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
    config.setKdfIter(4000);
    config.setFastKdfIter(2);
    config.setHmacUse(true);
    config.setHmacPgno(HmacPgno.LITTLE_ENDIAN);
    config.setHmacSaltMask(0x3a);
    config.setLegacy(2);
    config.setLegacyPageSize(1024);
    config.setKdfAlgorithm(KdfAlgorithm.SHA1);
    config.setHmacAlgorithm(HmacAlgorithm.SHA1);
    return config;
  }

  public static SQLiteMCSqlCipherConfig getV3Defaults() {
    SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
    config.setKdfIter(64000);
    config.setFastKdfIter(2);
    config.setHmacUse(true);
    config.setHmacPgno(HmacPgno.LITTLE_ENDIAN);
    config.setHmacSaltMask(0x3a);
    config.setLegacy(3);
    config.setLegacyPageSize(1024);
    config.setKdfAlgorithm(KdfAlgorithm.SHA1);
    config.setHmacAlgorithm(HmacAlgorithm.SHA1);
    return  config;
  }

  public static SQLiteMCSqlCipherConfig getV4Defaults() {
    SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
    config.setKdfIter(256000);
    config.setFastKdfIter(2);
    config.setHmacUse(true);
    config.setHmacPgno(HmacPgno.LITTLE_ENDIAN);
    config.setHmacSaltMask(0x3a);
    config.setLegacy(0);
    config.setLegacyPageSize(4096);
    config.setKdfAlgorithm(KdfAlgorithm.SHA512);
    config.setHmacAlgorithm(HmacAlgorithm.SHA512);
    config.setPlaintextHeaderSize(0);
    return config;
  }

}
